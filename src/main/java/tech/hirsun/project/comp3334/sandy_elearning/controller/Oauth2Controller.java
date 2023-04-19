package tech.hirsun.project.comp3334.sandy_elearning.controller;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import tech.hirsun.project.comp3334.sandy_elearning.common.Constants;
import tech.hirsun.project.comp3334.sandy_elearning.common.Result;
import tech.hirsun.project.comp3334.sandy_elearning.common.ResultGenerator;
import tech.hirsun.project.comp3334.sandy_elearning.entity.GeneralUser;
import tech.hirsun.project.comp3334.sandy_elearning.service.GeneralUserService;
import tech.hirsun.project.comp3334.sandy_elearning.utils.NumberUtil;
import tech.hirsun.project.comp3334.sandy_elearning.utils.SystemUtil;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

import static tech.hirsun.project.comp3334.sandy_elearning.common.Constants.RESULT_CODE_PARAM_ERROR;

@RestController
@RequestMapping("/oauth2")
public class Oauth2Controller implements HandlerInterceptor {

    @Value("${azure.ad.tenant-id}")
    private String tenantId;

    @Value("${azure.ad.client-id}")
    private String clientId;

    @Value("${azure.ad.client-secret}")
    private String clientSecret;

    @Value("${azure.ad.redirect-uri}")
    private String redirectUri;

    @Value("${azure.ad.email-suffix}")
    private String emailSuffix;

    @Autowired
    private GeneralUserService generalUserService;

    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
    public ResponseEntity<String> authorize() {
        String authUrl = "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/authorize" +
                "?client_id=" + clientId +
                "&response_type=code" +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&response_mode=query" +
                "&scope=openid+profile+email" +
                "&state=12345";

        return ResponseEntity.status(HttpStatus.FOUND).header("Location", authUrl).build();
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    public Result callback(@RequestBody Oauth2Code code) throws ParseException {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", clientId);
        formData.add("code", code.getCode());
        formData.add("redirect_uri", redirectUri);
        formData.add("client_secret", clientSecret);

        ResponseEntity<JSONObject> response = restTemplate.postForEntity(
                "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token",
                formData, JSONObject.class);

        JSONObject responseBody = response.getBody();
        Result result = ResultGenerator.genErrorResult(Constants.RESULT_CODE_SERVER_ERROR, "Server Error!");


        if (responseBody != null) {
            String idToken = responseBody.getAsString("id_token");
            SignedJWT jwt = SignedJWT.parse(idToken);
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();

            String email = claimsSet.getClaim("email").toString();
            String displayName = claimsSet.getClaim("name").toString();

            // Right emailSuffix
            if (email.endsWith(emailSuffix)){

                // if user does not exist
                if (generalUserService.queryByName(email) == null){
                    GeneralUser tempGeneralUser = new GeneralUser();
                    //Add user to database
                    tempGeneralUser.setUserName(email);
                    String token = SystemUtil.genToken(System.currentTimeMillis() + String.valueOf(tempGeneralUser.getId()) + NumberUtil.genRandomNum(4));
                    tempGeneralUser.setPassword(token);
                    generalUserService.add(tempGeneralUser);
                }

                // login
                GeneralUser generalUser = generalUserService.oauth2Login(email);
                if (generalUser != null) {
                    result = ResultGenerator.genSuccessResult(generalUser);
                    return result;

                }
            }else {
                result = ResultGenerator.genErrorResult(RESULT_CODE_PARAM_ERROR,"Illegal User!");
            }

        }

        return result;

    }
}

@Getter
@Setter
class Oauth2Code implements java.io.Serializable{
    private String code;
}




