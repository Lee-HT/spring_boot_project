# Letsencrypt 인증서 재발급

certbot renew
cd /etc/letsencrypt/live/codelia.shop/ || exit
expect <<EOF
spawn sudo openssl pkcs12 -export -in fullchain.pem -inkey privkey.pem -out keystore.p12 -name tomcat -CAfile chain.pem -caname root
expect "Password:"
send "135792468a\r"
expect "Password:"
send "135792468a\r"
expect eof
EOF
aws s3 cp keystore.p12 s3://aws-github-action-bucket/SSL_KEY/
