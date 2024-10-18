package com.rs.demo2.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.rs.demo2.dto.request.AuthenticationRequest;
import com.rs.demo2.dto.request.IntrospectRequest;
import com.rs.demo2.dto.request.LogoutRequest;
import com.rs.demo2.dto.request.RefreshRequest;
import com.rs.demo2.dto.response.AuthenticationResponse;
import com.rs.demo2.dto.response.IntrospectResponse;
import com.rs.demo2.entity.InvalidatedToken;
import com.rs.demo2.entity.User;
import com.rs.demo2.exception.AppException;
import com.rs.demo2.exception.ErrorCode;
import com.rs.demo2.repository.InvalidatedTokenRepository;
import com.rs.demo2.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
	UserRepository userRepository;

	InvalidatedTokenRepository invalidatedTokenRepository;

	@NonFinal // khong inject no vao constructor
	@Value("${jwt.signerKey}")
	protected String SIGNER_KEY;

	@NonFinal // khong inject no vao constructor
	@Value("${jwt.valid-duration}")
	protected long VALID_DURATION;

	@NonFinal // khong inject no vao constructor
	@Value("${jwt.refreshable-duration}")
	protected long REFRESHABLE_DURATION;

	public void logout(LogoutRequest request) throws ParseException, JOSEException {
		try {
			var signToken = verifyToken(request.getToken(), false);

			String jit = signToken.getJWTClaimsSet().getJWTID();

			// giai thich them tai sao o day ta lai dung getExpirationTime (thoi diem token het han) chu khong phai la
			// thoi diem ta vo hieu hoa no.
			// muc dich chinh cua no la ta dung de clean up cac du lieu trong bang, dung mysql schedule de don dep no,
			// nghia la sau mot khong thoi gian
			// co dinh, chang han ta se thuc hien cau lenh mysql de don dep du lieu, tranh gay ra lang phi bo nho. Tai
			// sao lai la thoi diem token het han
			// boi gia su neu thoi dung thoi gian la thoi gian logout la 13h chang han, gia su sau 14h thi ta thuc hien
			// don dep data trong CSDL
			// ma token ta lai co hieu luc la 3h dong ho, gia su nhu ta dang nhap luc 12h thi luc nay vao thoi diem 14h
			// ta thuc hien xoa data
			// nghia la token nay chua het han ma da bi xoa roi, nghia la sau 14h thi ta co the dung token nay de dang
			// nhap cho den het 15h.
			// day la tai sao ta phai lay thoi gian het han de don dep.
			Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

			InvalidatedToken invalidatedToken =
					InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();
			invalidatedTokenRepository.save(invalidatedToken);
		} catch (AppException e) {
			// khoi catch nay duoc thuc thi khi cau lenh try no nem ra ngoai le, nghia la neu verify la false thi cau
			// lenh duoi day duoc thuc thi,
			// noi chung la tat ca truong hop nem ngoai le trong cau lenh try nem ra throw thi deu cac cau lenh duoi day
			// deu duoc thuc thi het.
			log.info("Token already expired");
		}
	}

	// ham nay lay thong tin tu token va check ca ma token va thoi gian, check ca token da bi logout chua
	SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
		JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

		SignedJWT signedJWT = SignedJWT.parse(token); // cau lenh nay dung de chuyen doi token thanh dang SignedJWT

		Date expirationTime = isRefresh
				? Date.from(signedJWT
						.getJWTClaimsSet()
						.getIssueTime()
						.toInstant()
						.plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS))
				: signedJWT.getJWTClaimsSet().getExpirationTime();
		var verified = signedJWT.verify(verifier);

		if (!verified || !expirationTime.after(new Date())) throw new AppException(ErrorCode.UNAUTHENTICATED);
		// bieu thuc tren co the chuyen thanh : nay la dung De Morgan's Law: !(A && B) into !A || !B
		// don gian giai thich nhu sau: A&&B nghia la A va B deu dung thi no moi thuc hien, phu dinh cua no
		// nghia la hoac A sai, hoac B sai thi ca 2 cung deu duoc thuc hien
		// if (!(verified && expirationTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

		if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
			throw new AppException(ErrorCode.UNAUTHENTICATED);

		return signedJWT;
	}

	String generateToken(User user) {
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

		JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
				.subject(user.getUserName())
				.issuer("demo2")
				.issueTime(new Date())
				.expirationTime(new Date(
						Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
				.jwtID(UUID.randomUUID().toString())
				.claim("scope", buildScope(user))
				.build();

		Payload payload = new Payload(jwtClaimsSet.toJSONObject());

		JWSObject jwsObject = new JWSObject(header, payload);

		try {
			jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
			return jwsObject.serialize();
		} catch (JOSEException e) {
			throw new RuntimeException(e);
		}
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		var user = userRepository
				.findByUserName(request.getUserName())
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
		// mac du dua ta da khai bao passwordEncoder la @Bean va khi can dung ta chi can @Autowire ma dung thoi, nhung
		// neu dua ra ngoai ta
		// se gap loi boi vi cac bean no require lan nhau , xem comment tren youtube: Cái này do 2 bean require lẫn nhau
		// nên bị, em cần tránh việc đó.
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
		boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

		// tat dinh dang spotless o doan code duoi day
		// spotless: off
		if (!authenticated) {
								throw new AppException(ErrorCode.UNAUTHENTICATED);
		}
		// spotless:on

		var token = generateToken(user);

		return AuthenticationResponse.builder().token(token).authenticated(true).build();
	}

	public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
		var token = request.getToken();
		boolean isValid = true;

		try {
			verifyToken(token, false);
		} catch (AppException e) {
			isValid = false;
		}
		return IntrospectResponse.builder().valid(isValid).build();
	}

	// chu thich them tai sao cai refreshToken chi duoc tao 1 token moi voi 1 token ban dau: vi khi refresh token moi
	// thi ta
	// da logout token do nen ta khong the su dung lai no de tao ra token moi lan nua
	public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
		// RefreshToken co nghia la khi token sap het thoi gian su dung thi ta se tao ra mot token moi de giup
		// user co the tiep tuc dang nhap tiep ma khong can phai login, tranh lam anh huong trai nghiem nguoi dung

		// thu tu thuc hien cua no nhu sau: 1.kiem tra token da dung va con han su dung ko, co bi logout chua
		// 2.Tiep den la logout cai token cu nay
		// 3.Tim kiem user dua tren cai userName da luu trong token duoi dang sub-> cai nay hiem khi xay ra
		// boi vi thuc chat buoc 1 da kiem tra roi, nhung co the xay ra khi mang loi, khi do ko the check data trong
		// databse duoc
		// 4. tao ra mot cai token moi
		var signedJWT = verifyToken(request.getToken(), true);

		String jit = signedJWT.getJWTClaimsSet().getJWTID();
		Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

		InvalidatedToken invalidatedToken =
				InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();
		invalidatedTokenRepository.save(invalidatedToken);

		String username = signedJWT.getJWTClaimsSet().getSubject();

		User user =
				userRepository.findByUserName(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

		String token = generateToken(user);

		return AuthenticationResponse.builder().token(token).authenticated(true).build();
	}

	private String buildScope(User user) {
		StringJoiner stringJoiner = new StringJoiner(" ");

		if (!CollectionUtils.isEmpty(user.getRoles()))
			user.getRoles().forEach(role -> {
				stringJoiner.add("ROLE_" + role.getName());
				//  stringJoiner.add(role.getName());

				// them permission vao trong thang SCOPE cua jwt
				if (!CollectionUtils.isEmpty(role.getPermissions()))
					role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
			});

		return stringJoiner.toString();
	}
}
