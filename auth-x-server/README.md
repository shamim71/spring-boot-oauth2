# Authentication server build instructions

[Creating build]
gradle eclipse clean build
 
[Running boot application]
gradle bootRun
 
[How to get access token using password flow]
curl web-appx:secret@localhost:9991/auth/oauth/token -d grant_type=password -d username=[Username] -d password=[Password]

[How to refresh token]
curl web-appx:secret@localhost:9991/auth/oauth/token -d  "grant_type=refresh_token&jti=[JTI]&refresh_token=[REFRESH TOKEN]"

[Generate JKS Java KeyStore File]

$ keytool -genkeypair -v \
        -alias myappteam.com \
        -keyalg RSA \
        -keysize 4096 \
        -validity 3650 \
        -keypass MyAppPass123# \
        -storepass MyAppPass123# \
        -keystore jwt_key_store.jks
 

[Export Public Key]
 
$ keytool -list -rfc --keystore jwt_key_store.jks | openssl x509 -inform pem -pubkey
 