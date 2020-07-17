#! /bin/sh

KEY_SIZE=${1:-4096}

EMAIL_NAME=${2:-email}
EMAIL_ALIAS=${3:-email}
EMAIL_KEYSTORE_PASSWORD=${4:-password}

RMQ_NAME=${14:-rabbitmq}

AUTH_NAME=${15:-auth}
AUTH_ALIAS=${16:-auth}
AUTH_KEYSTORE_PASSWORD=${17:-password}

mkdir -p "certs/${EUREKA_NAME}/keystore"

# Create TLS email request
SAN=DNS:tim2.xws,DNS:host.docker.internal,DNS:localhost,IP:172.20.0.0,IP:26.206.31.193,IP:10.0.2.15,IP:192.168.1.7,IP:192.168.99.1,IP:127.0.0.1 \
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config etc/server.conf \
    -out "certs/${EMAIL_NAME}/${EMAIL_NAME}.csr" \
    -keyout "certs/${EMAIL_NAME}/${EMAIL_NAME}.key" \
    -subj "/C=NO/O=BSEP/OU=Tim 2/CN=email" || exit

# Create TLS email certificate
openssl ca \
    -batch \
    -config etc/tls-ca.conf \
    -in "certs/${EMAIL_NAME}/${EMAIL_NAME}.csr" \
    -out "certs/${EMAIL_NAME}/${EMAIL_NAME}.crt" \
    -extensions email_ext || exit

# Export TLS email certificate chain to keystore
openssl pkcs12 \
    -export \
    -name "${EMAIL_ALIAS}" \
    -inkey "certs/${EMAIL_NAME}/${EMAIL_NAME}.key" \
    -in "certs/${EMAIL_NAME}/${EMAIL_NAME}.crt" \
    -certfile ca/tls-ca-chain.pem \
    -passout "pass:${EMAIL_KEYSTORE_PASSWORD}" \
    -out "certs/${EMAIL_NAME}/keystore/${EMAIL_NAME}.keystore.p12" || exit

# Import Root CA, TLS CA, eureka, auth, advertisement, message, order and client certificate into zuul keystore
keytool \
    -importcert \
    -noprompt \
    -alias "${AUTH_ALIAS}" \
    -file "certs/${AUTH_NAME}/${AUTH_NAME}.crt" \
    -storepass "${EMAIL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EMAIL_NAME}/keystore/${EMAIL_NAME}.truststore.p12"  && \

keytool \
    -importcert \
    -noprompt \
    -alias "${RMQ_NAME}" \
    -file "certs/${RMQ_NAME}/${RMQ_NAME}.crt" \
    -storepass "${EMAIL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EMAIL_NAME}/keystore/${EMAIL_NAME}.truststore.p12"  || exit
