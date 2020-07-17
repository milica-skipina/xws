#! /bin/sh

KEY_SIZE=${1:-4096}

EUREKA_NAME=${2:-eureka}
EUREKA_ALIAS=${3:-eureka}
EUREKA_KEYSTORE_PASSWORD=${4:-soMe_StrOng12_PaSS}

ZUUL_NAME=${5:-zuul}
ZUUL_ALIAS=${6:-zuul}
ZUUL_KEYSTORE_PASSWORD=${7:-soMe_StrOng12_PaSS}

CLIENT_NAME=${8:-client}
CLIENT_ALIAS=${9:-client}
CLIENT_KEYSTORE_PASSWORD=${10:-soMe_StrOng12_PaSS}

AGENT_NAME=${11:-agent}
AGENT_ALIAS=${12:-agent}
AGENT_KEYSTORE_PASSWORD=${13:-soMe_StrOng12_PaSS}

RMQ_NAME=${14:-rabbitmq}

AUTH_NAME=${15:-auth}
AUTH_ALIAS=${16:-auth}
AUTH_KEYSTORE_PASSWORD=${17:-soMe_StrOng12_PaSS}

MESSAGE_NAME=${18:-message}
MESSAGE_ALIAS=${19:-message}
MESSAGE_KEYSTORE_PASSWORD=${20:-soMe_StrOng12_PaSS}

ORDER_NAME=${21:-order}
ORDER_ALIAS=${22:-order}
ORDER_KEYSTORE_PASSWORD=${23:-soMe_StrOng12_PaSS}

ADVERT_NAME=${24:-advertisement}
ADVERT_ALIAS=${25:-advertisement}
ADVERT_KEYSTORE_PASSWORD=${26:-soMe_StrOng12_PaSS}

EMAIL_NAME=${27:-email}
EMAIL_ALIAS=${28:-email}
EMAIL_KEYSTORE_PASSWORD=${29:-soMe_StrOng12_PaSS}


# Root CA
mkdir -p ca/root-ca/private ca/root-ca/db crl certs
chmod 700 ca/root-ca/private

# Create database for Root CA
cp /dev/null ca/root-ca/db/root-ca.db
cp /dev/null ca/root-ca/db/root-ca.db.attr
echo 01 > ca/root-ca/db/root-ca.crt.srl
echo 01 > ca/root-ca/db/root-ca.crl.srl

# Create Root CA request
openssl req \
    -new \
    -nodes \
    -config etc/root-ca.conf \
    -out ca/root-ca.csr \
    -keyout ca/root-ca/private/root-ca.key || exit

# Create Root CA certificate
openssl ca \
    -selfsign \
    -batch \
    -config etc/root-ca.conf \
    -in ca/root-ca.csr \
    -out ca/root-ca.crt \
    -extensions root_ca_ext || exit

# Create initial CRL
openssl ca \
    -gencrl \
    -config etc/root-ca.conf \
    -out crl/root-ca.crl || exit

# TLS CA
mkdir -p ca/tls-ca/private ca/tls-ca/db crl certs
chmod 700 ca/tls-ca/private

# Create database for TLS CA
cp /dev/null ca/tls-ca/db/tls-ca.db
cp /dev/null ca/tls-ca/db/tls-ca.db.attr
echo 01 > ca/tls-ca/db/tls-ca.crt.srl
echo 01 > ca/tls-ca/db/tls-ca.crl.srl

# Create TLS CA request
openssl req \
    -new \
    -nodes \
    -config etc/tls-ca.conf \
    -out ca/tls-ca.csr \
    -keyout ca/tls-ca/private/tls-ca.key || exit

# Create TLS CA certificate
openssl ca \
    -batch \
    -config etc/root-ca.conf \
    -in ca/tls-ca.csr \
    -out ca/tls-ca.crt \
    -extensions signing_ca_ext || exit

# Create initial CRL
openssl ca \
    -gencrl \
    -config etc/tls-ca.conf \
    -out crl/tls-ca.crl || exit

cat ca/tls-ca.crt ca/root-ca.crt > \
    ca/tls-ca-chain.pem

mkdir -p "certs/${EUREKA_NAME}/keystore"

# Create TLS server request
SAN=DNS:tim2.xws,DNS:host.docker.internal,DNS:localhost,DNS:eureka,IP:172.20.0.0,IP:26.206.31.193,IP:10.0.2.15,IP:192.168.1.7,IP:192.168.99.1,IP:127.0.0.1 \
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config etc/server.conf \
    -out "certs/${EUREKA_NAME}/${EUREKA_NAME}.csr" \
    -keyout "certs/${EUREKA_NAME}/${EUREKA_NAME}.key" \
    -subj "/C=NO/O=BSEP/OU=Tim 2/CN=eureka" || exit

# Create TLS server certificate
openssl ca \
    -batch \
    -config etc/tls-ca.conf \
    -in "certs/${EUREKA_NAME}/${EUREKA_NAME}.csr" \
    -out "certs/${EUREKA_NAME}/${EUREKA_NAME}.crt" \
    -extensions server_ext || exit

# Export TLS server certificate chain to keystore
openssl pkcs12 \
    -export \
    -name "${EUREKA_ALIAS}" \
    -inkey "certs/${EUREKA_NAME}/${EUREKA_NAME}.key" \
    -in "certs/${EUREKA_NAME}/${EUREKA_NAME}.crt" \
    -certfile ca/tls-ca-chain.pem \
    -passout "pass:${EUREKA_KEYSTORE_PASSWORD}" \
    -out "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.keystore.p12" || exit


mkdir -p "certs/${ZUUL_NAME}/keystore"

# Create TLS report request
SAN=DNS:tim2.xws,DNS:host.docker.internal,DNS:localhost,DNS:eureka,IP:172.20.0.0,IP:26.206.31.193,IP:10.0.2.15,IP:192.168.1.7,IP:192.168.99.1,IP:127.0.0.1 \
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config etc/server.conf \
    -out "certs/${ZUUL_NAME}/${ZUUL_NAME}.csr" \
    -keyout "certs/${ZUUL_NAME}/${ZUUL_NAME}.key" \
    -subj "/C=NO/O=BSEP/OU=Tim 2/CN=zuul" || exit

# Create TLS report certificate
openssl ca \
    -batch \
    -config etc/tls-ca.conf \
    -in "certs/${ZUUL_NAME}/${ZUUL_NAME}.csr" \
    -out "certs/${ZUUL_NAME}/${ZUUL_NAME}.crt" \
    -extensions server_ext || exit

# Export TLS report certificate chain to keystore
openssl pkcs12 \
    -export \
    -name "${ZUUL_ALIAS}" \
    -inkey "certs/${ZUUL_NAME}/${ZUUL_NAME}.key" \
    -in "certs/${ZUUL_NAME}/${ZUUL_NAME}.crt" \
    -certfile ca/tls-ca-chain.pem \
    -passout "pass:${ZUUL_KEYSTORE_PASSWORD}" \
    -out "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.keystore.p12" || exit


mkdir -p "certs/${AGENT_NAME}/keystore"

# Create TLS agent request
SAN=DNS:tim2.xws,DNS:host.docker.internal,DNS:localhost,DNS:eureka,IP:172.20.0.0,IP:26.206.31.193,IP:10.0.2.15,IP:192.168.1.7,IP:192.168.99.1,IP:127.0.0.1 \
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config etc/server.conf \
    -out "certs/${AGENT_NAME}/${AGENT_NAME}.csr" \
    -keyout "certs/${AGENT_NAME}/${AGENT_NAME}.key" \
    -subj "/C=NO/O=BSEP/OU=Tim 2/CN=logan" || exit

# Create TLS agent certificate
openssl ca \
    -batch \
    -config etc/tls-ca.conf \
    -in "certs/${AGENT_NAME}/${AGENT_NAME}.csr" \
    -out "certs/${AGENT_NAME}/${AGENT_NAME}.crt" \
    -extensions server_ext || exit

# Export TLS agent certificate chain to keystore
openssl pkcs12 \
    -export \
    -name "${AGENT_ALIAS}" \
    -inkey "certs/${AGENT_NAME}/${AGENT_NAME}.key" \
    -in "certs/${AGENT_NAME}/${AGENT_NAME}.crt" \
    -certfile ca/tls-ca-chain.pem \
    -passout "pass:${AGENT_KEYSTORE_PASSWORD}" \
    -out "certs/${AGENT_NAME}/keystore/${AGENT_NAME}.keystore.p12" || exit

mkdir -p "certs/${EMAIL_NAME}/keystore"

# Create TLS email request
SAN=DNS:tim2.xws,DNS:host.docker.internal,DNS:localhost,DNS:eureka,IP:172.20.0.0,IP:26.206.31.193,IP:10.0.2.15,IP:192.168.1.7,IP:192.168.99.1,IP:127.0.0.1 \
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config etc/server.conf \
    -out "certs/${EMAIL_NAME}/${EMAIL_NAME}.csr" \
    -keyout "certs/${EMAIL_NAME}/${EMAIL_NAME}.key" \
    -subj "/C=NO/O=BSEP/OU=Tim 2/CN=logan" || exit

# Create TLS agent certificate
openssl ca \
    -batch \
    -config etc/tls-ca.conf \
    -in "certs/${EMAIL_NAME}/${EMAIL_NAME}.csr" \
    -out "certs/${EMAIL_NAME}/${EMAIL_NAME}.crt" \
    -extensions server_ext || exit

# Export TLS agent certificate chain to keystore
openssl pkcs12 \
    -export \
    -name "${EMAIL_ALIAS}" \
    -inkey "certs/${EMAIL_NAME}/${EMAIL_NAME}.key" \
    -in "certs/${EMAIL_NAME}/${EMAIL_NAME}.crt" \
    -certfile ca/tls-ca-chain.pem \
    -passout "pass:${EMAIL_KEYSTORE_PASSWORD}" \
    -out "certs/${EMAIL_NAME}/keystore/${EMAIL_NAME}.keystore.p12" || exit

mkdir -p "certs/${ADVERT_NAME}/keystore"

# Create TLS server request
SAN=DNS:tim2.xws,DNS:host.docker.internal,DNS:localhost,DNS:eureka,IP:172.20.0.0,IP:26.206.31.193,IP:10.0.2.15,IP:192.168.1.7,IP:192.168.99.1,IP:127.0.0.1 \
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config etc/server.conf \
    -out "certs/${ADVERT_NAME}/${ADVERT_NAME}.csr" \
    -keyout "certs/${ADVERT_NAME}/${ADVERT_NAME}.key" \
    -subj "/C=NO/O=BSEP/OU=Tim 2/CN=advertisement" || exit

# Create TLS server certificate
openssl ca \
    -batch \
    -config etc/tls-ca.conf \
    -in "certs/${ADVERT_NAME}/${ADVERT_NAME}.csr" \
    -out "certs/${ADVERT_NAME}/${ADVERT_NAME}.crt" \
    -extensions server_ext || exit

# Export TLS server certificate chain to keystore
openssl pkcs12 \
    -export \
    -name "${ADVERT_ALIAS}" \
    -inkey "certs/${ADVERT_NAME}/${ADVERT_NAME}.key" \
    -in "certs/${ADVERT_NAME}/${ADVERT_NAME}.crt" \
    -certfile ca/tls-ca-chain.pem \
    -passout "pass:${ADVERT_KEYSTORE_PASSWORD}" \
    -out "certs/${ADVERT_NAME}/keystore/${ADVERT_NAME}.keystore.p12" || exit

mkdir -p "certs/${AUTH_NAME}/keystore"

# Create TLS server request
SAN=DNS:tim2.xws,DNS:host.docker.internal,DNS:localhost,DNS:eureka,IP:172.20.0.0,IP:26.206.31.193,IP:10.0.2.15,IP:192.168.1.7,IP:192.168.99.1,IP:127.0.0.1 \
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config etc/server.conf \
    -out "certs/${AUTH_NAME}/${AUTH_NAME}.csr" \
    -keyout "certs/${AUTH_NAME}/${AUTH_NAME}.key" \
    -subj "/C=NO/O=BSEP/OU=Tim 2/CN=auth" || exit

# Create TLS server certificate
openssl ca \
    -batch \
    -config etc/tls-ca.conf \
    -in "certs/${AUTH_NAME}/${AUTH_NAME}.csr" \
    -out "certs/${AUTH_NAME}/${AUTH_NAME}.crt" \
    -extensions server_ext || exit

# Export TLS server certificate chain to keystore
openssl pkcs12 \
    -export \
    -name "${AUTH_ALIAS}" \
    -inkey "certs/${AUTH_NAME}/${AUTH_NAME}.key" \
    -in "certs/${AUTH_NAME}/${AUTH_NAME}.crt" \
    -certfile ca/tls-ca-chain.pem \
    -passout "pass:${AUTH_KEYSTORE_PASSWORD}" \
    -out "certs/${AUTH_NAME}/keystore/${AUTH_NAME}.keystore.p12" || exit

mkdir -p "certs/${ORDER_NAME}/keystore"

# Create TLS server request
SAN=DNS:tim2.xws,DNS:host.docker.internal,DNS:localhost,DNS:eureka,IP:172.20.0.0,IP:26.206.31.193,IP:10.0.2.15,IP:192.168.1.7,IP:192.168.99.1,IP:127.0.0.1 \
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config etc/server.conf \
    -out "certs/${ORDER_NAME}/${ORDER_NAME}.csr" \
    -keyout "certs/${ORDER_NAME}/${ORDER_NAME}.key" \
    -subj "/C=NO/O=BSEP/OU=Tim 2/CN=order" || exit

# Create TLS server certificate
openssl ca \
    -batch \
    -config etc/tls-ca.conf \
    -in "certs/${ORDER_NAME}/${ORDER_NAME}.csr" \
    -out "certs/${ORDER_NAME}/${ORDER_NAME}.crt" \
    -extensions server_ext || exit

# Export TLS server certificate chain to keystore
openssl pkcs12 \
    -export \
    -name "${ORDER_ALIAS}" \
    -inkey "certs/${ORDER_NAME}/${ORDER_NAME}.key" \
    -in "certs/${ORDER_NAME}/${ORDER_NAME}.crt" \
    -certfile ca/tls-ca-chain.pem \
    -passout "pass:${ORDER_KEYSTORE_PASSWORD}" \
    -out "certs/${ORDER_NAME}/keystore/${ORDER_NAME}.keystore.p12" || exit

mkdir -p "certs/${MESSAGE_NAME}/keystore"

# Create TLS message request
SAN=DNS:tim2.xws,DNS:host.docker.internal,DNS:localhost,DNS:eureka,IP:172.20.0.0,IP:26.206.31.193,IP:10.0.2.15,IP:192.168.1.7,IP:192.168.99.1,IP:127.0.0.1 \
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config etc/server.conf \
    -out "certs/${MESSAGE_NAME}/${MESSAGE_NAME}.csr" \
    -keyout "certs/${MESSAGE_NAME}/${MESSAGE_NAME}.key" \
    -subj "/C=NO/O=BSEP/OU=Tim 2/CN=message" || exit

# Create TLS message certificate
openssl ca \
    -batch \
    -config etc/tls-ca.conf \
    -in "certs/${MESSAGE_NAME}/${MESSAGE_NAME}.csr" \
    -out "certs/${MESSAGE_NAME}/${MESSAGE_NAME}.crt" \
    -extensions server_ext || exit

# Export TLS message certificate chain to keystore
openssl pkcs12 \
    -export \
    -name "${MESSAGE_ALIAS}" \
    -inkey "certs/${MESSAGE_NAME}/${MESSAGE_NAME}.key" \
    -in "certs/${MESSAGE_NAME}/${MESSAGE_NAME}.crt" \
    -certfile ca/tls-ca-chain.pem \
    -passout "pass:${MESSAGE_KEYSTORE_PASSWORD}" \
    -out "certs/${MESSAGE_NAME}/keystore/${MESSAGE_NAME}.keystore.p12" || exit

mkdir -p "certs/${RMQ_NAME}"

# Create TLS rabbitmq request
SAN=DNS:tim2.xws,DNS:host.docker.internal,DNS:localhost,DNS:eureka,IP:172.20.0.0,IP:26.206.31.193,IP:10.0.2.15,IP:192.168.1.7,IP:192.168.99.1,IP:127.0.0.1 \
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config etc/server.conf \
    -out "certs/${RMQ_NAME}/${RMQ_NAME}.csr" \
    -keyout "certs/${RMQ_NAME}/${RMQ_NAME}.key" \
    -subj "/C=NO/O=BSEP/OU=Tim 2/CN=rabbitmq" || exit

# Create TLS rabbitmq certificate
openssl ca \
    -batch \
    -config etc/tls-ca.conf \
    -in "certs/${RMQ_NAME}/${RMQ_NAME}.csr" \
    -out "certs/${RMQ_NAME}/${RMQ_NAME}.crt" \
    -extensions server_ext || exit

cp ca/tls-ca-chain.pem "certs/${RMQ_NAME}"


mkdir -p "certs/${CLIENT_NAME}"

# Create TLS client request (for web browser)
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config etc/client.conf \
    -out "certs/${CLIENT_NAME}/${CLIENT_NAME}.csr" \
    -keyout "certs/${CLIENT_NAME}/${CLIENT_NAME}.key" \
    -subj "/C=NO/O=BSEP/OU=Tim 2/CN=Peter Peterson" || exit
    
# Create TLS client certificate (for web browser)
openssl ca \
    -batch \
    -config etc/tls-ca.conf \
    -in "certs/${CLIENT_NAME}/${CLIENT_NAME}.csr" \
    -out "certs/${CLIENT_NAME}/${CLIENT_NAME}.crt" \
    -policy extern_pol \
    -extensions client_ext || exit

# Export TLS client certificate chain to keystore
openssl pkcs12 \
    -export \
    -name "${CLIENT_ALIAS}" \
    -inkey "certs/${CLIENT_NAME}/${CLIENT_NAME}.key" \
    -in "certs/${CLIENT_NAME}/${CLIENT_NAME}.crt" \
    -certfile ca/tls-ca-chain.pem \
    -passout "pass:${CLIENT_KEYSTORE_PASSWORD}" \
    -out "certs/${CLIENT_NAME}/${CLIENT_NAME}.p12" || exit


# Import Root CA, TLS CA, zuul, auth, advertisement, message, order and client certificate into eureka keystore
keytool \
    -importcert \
    -noprompt \
    -alias "root" \
    -file ca/root-ca.crt \
    -storepass "${EUREKA_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "tls" \
    -file ca/tls-ca.crt \
    -storepass "${EUREKA_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${ZUUL_ALIAS}" \
    -file "certs/${ZUUL_NAME}/${ZUUL_NAME}.crt" \
    -storepass "${EUREKA_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${ADVERT_ALIAS}" \
    -file "certs/${ADVERT_NAME}/${ADVERT_NAME}.crt" \
    -storepass "${EUREKA_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${AUTH_ALIAS}" \
    -file "certs/${AUTH_NAME}/${AUTH_NAME}.crt" \
    -storepass "${EUREKA_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${MESSAGE_ALIAS}" \
    -file "certs/${MESSAGE_NAME}/${MESSAGE_NAME}.crt" \
    -storepass "${EUREKA_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${ORDER_ALIAS}" \
    -file "certs/${ORDER_NAME}/${ORDER_NAME}.crt" \
    -storepass "${EUREKA_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLIENT_ALIAS}" \
    -file "certs/${CLIENT_NAME}/${CLIENT_NAME}.crt" \
    -storepass "${EUREKA_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.truststore.p12"  && \


# Import Root CA, TLS CA, eureka, auth, advertisement, message, order and client certificate into zuul keystore
keytool \
    -importcert \
    -noprompt \
    -alias "root" \
    -file ca/root-ca.crt \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "tls" \
    -file ca/tls-ca.crt \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${EUREKA_ALIAS}" \
    -file "certs/${EUREKA_NAME}/${EUREKA_NAME}.crt" \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${ADVERT_ALIAS}" \
    -file "certs/${ADVERT_NAME}/${ADVERT_NAME}.crt" \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${AUTH_ALIAS}" \
    -file "certs/${AUTH_NAME}/${AUTH_NAME}.crt" \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${MESSAGE_ALIAS}" \
    -file "certs/${MESSAGE_NAME}/${MESSAGE_NAME}.crt" \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${ORDER_ALIAS}" \
    -file "certs/${ORDER_NAME}/${ORDER_NAME}.crt" \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLIENT_ALIAS}" \
    -file "certs/${CLIENT_NAME}/${CLIENT_NAME}.crt" \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${RMQ_NAME}" \
    -file "certs/${RMQ_NAME}/${RMQ_NAME}.crt" \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${AGENT_NAME}" \
    -file "certs/${AGENT_NAME}/${AGENT_NAME}.crt" \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12"  && \



# Import Root CA, TLS CA, zuul, eureka, auth, message, order and client certificate into advertisement keystore
keytool \
    -importcert \
    -noprompt \
    -alias "root" \
    -file ca/root-ca.crt \
    -storepass "${ADVERT_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ADVERT_NAME}/keystore/${ADVERT_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "tls" \
    -file ca/tls-ca.crt \
    -storepass "${ADVERT_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ADVERT_NAME}/keystore/${ADVERT_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${EUREKA_ALIAS}" \
    -file "certs/${EUREKA_NAME}/${EUREKA_NAME}.crt" \
    -storepass "${ADVERT_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ADVERT_NAME}/keystore/${ADVERT_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${ZUUL_ALIAS}" \
    -file "certs/${ZUUL_NAME}/${ZUUL_NAME}.crt" \
    -storepass "${ADVERT_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ADVERT_NAME}/keystore/${ADVERT_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${AUTH_ALIAS}" \
    -file "certs/${AUTH_NAME}/${AUTH_NAME}.crt" \
    -storepass "${ADVERT_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ADVERT_NAME}/keystore/${ADVERT_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${MESSAGE_ALIAS}" \
    -file "certs/${MESSAGE_NAME}/${MESSAGE_NAME}.crt" \
    -storepass "${ADVERT_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ADVERT_NAME}/keystore/${ADVERT_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${ORDER_ALIAS}" \
    -file "certs/${ORDER_NAME}/${ORDER_NAME}.crt" \
    -storepass "${ADVERT_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ADVERT_NAME}/keystore/${ADVERT_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLIENT_ALIAS}" \
    -file "certs/${CLIENT_NAME}/${CLIENT_NAME}.crt" \
    -storepass "${ADVERT_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ADVERT_NAME}/keystore/${ADVERT_NAME}.truststore.p12"  && \ 
keytool \
    -importcert \
    -noprompt \
    -alias "${RMQ_NAME}" \
    -file "certs/${RMQ_NAME}/${RMQ_NAME}.crt" \
    -storepass "${ADVERT_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ADVERT_NAME}/keystore/${ADVERT_NAME}.truststore.p12"  && \


# Import Root CA, TLS CA, zuul, eureka, advertisement, message, order and client certificate into auth keystore
keytool \
    -importcert \
    -noprompt \
    -alias "root" \
    -file ca/root-ca.crt \
    -storepass "${AUTH_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${AUTH_NAME}/keystore/${AUTH_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "tls" \
    -file ca/tls-ca.crt \
    -storepass "${AUTH_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${AUTH_NAME}/keystore/${AUTH_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${EUREKA_ALIAS}" \
    -file "certs/${EUREKA_NAME}/${EUREKA_NAME}.crt" \
    -storepass "${AUTH_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${AUTH_NAME}/keystore/${AUTH_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${ZUUL_ALIAS}" \
    -file "certs/${ZUUL_NAME}/${ZUUL_NAME}.crt" \
    -storepass "${AUTH_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${AUTH_NAME}/keystore/${AUTH_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${ADVERT_ALIAS}" \
    -file "certs/${ADVERT_NAME}/${ADVERT_NAME}.crt" \
    -storepass "${AUTH_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${AUTH_NAME}/keystore/${AUTH_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${MESSAGE_ALIAS}" \
    -file "certs/${MESSAGE_NAME}/${MESSAGE_NAME}.crt" \
    -storepass "${AUTH_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${AUTH_NAME}/keystore/${AUTH_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${ORDER_ALIAS}" \
    -file "certs/${ORDER_NAME}/${ORDER_NAME}.crt" \
    -storepass "${AUTH_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${AUTH_NAME}/keystore/${AUTH_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLIENT_ALIAS}" \
    -file "certs/${CLIENT_NAME}/${CLIENT_NAME}.crt" \
    -storepass "${AUTH_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${AUTH_NAME}/keystore/${AUTH_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${RMQ_NAME}" \
    -file "certs/${RMQ_NAME}/${RMQ_NAME}.crt" \
    -storepass "${AUTH_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${AUTH_NAME}/keystore/${AUTH_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${EMAIL_NAME}" \
    -file "certs/${EMAIL_NAME}/${EMAIL_NAME}.crt" \
    -storepass "${AUTH_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${AUTH_NAME}/keystore/${AUTH_NAME}.truststore.p12"  && \



# Import Root CA, TLS CA, zuul, eureka, advertisement, auth, order and client certificate into message keystore
keytool \
    -importcert \
    -noprompt \
    -alias "root" \
    -file ca/root-ca.crt \
    -storepass "${MESSAGE_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${MESSAGE_NAME}/keystore/${MESSAGE_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "tls" \
    -file ca/tls-ca.crt \
    -storepass "${MESSAGE_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${MESSAGE_NAME}/keystore/${MESSAGE_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${EUREKA_ALIAS}" \
    -file "certs/${EUREKA_NAME}/${EUREKA_NAME}.crt" \
    -storepass "${MESSAGE_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${MESSAGE_NAME}/keystore/${MESSAGE_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${ZUUL_ALIAS}" \
    -file "certs/${ZUUL_NAME}/${ZUUL_NAME}.crt" \
    -storepass "${MESSAGE_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${MESSAGE_NAME}/keystore/${MESSAGE_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${ADVERT_ALIAS}" \
    -file "certs/${ADVERT_NAME}/${ADVERT_NAME}.crt" \
    -storepass "${MESSAGE_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${MESSAGE_NAME}/keystore/${MESSAGE_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${AUTH_ALIAS}" \
    -file "certs/${AUTH_NAME}/${AUTH_NAME}.crt" \
    -storepass "${MESSAGE_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${MESSAGE_NAME}/keystore/${MESSAGE_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${ORDER_ALIAS}" \
    -file "certs/${ORDER_NAME}/${ORDER_NAME}.crt" \
    -storepass "${MESSAGE_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${MESSAGE_NAME}/keystore/${MESSAGE_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLIENT_ALIAS}" \
    -file "certs/${CLIENT_NAME}/${CLIENT_NAME}.crt" \
    -storepass "${MESSAGE_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${MESSAGE_NAME}/keystore/${MESSAGE_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${RMQ_NAME}" \
    -file "certs/${RMQ_NAME}/${RMQ_NAME}.crt" \
    -storepass "${MESSAGE_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${MESSAGE_NAME}/keystore/${MESSAGE_NAME}.truststore.p12"  && \



# Import Root CA, TLS CA, zuul, eureka, client certificate into agent keystore
keytool \
    -importcert \
    -noprompt \
    -alias "root" \
    -file ca/root-ca.crt \
    -storepass "${AGENT_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${AGENT_NAME}/keystore/${AGENT_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "tls" \
    -file ca/tls-ca.crt \
    -storepass "${AGENT_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${AGENT_NAME}/keystore/${AGENT_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${ZUUL_ALIAS}" \
    -file "certs/${ZUUL_NAME}/${ZUUL_NAME}.crt" \
    -storepass "${AGENT_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${AGENT_NAME}/keystore/${AGENT_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLIENT_ALIAS}" \
    -file "certs/${CLIENT_NAME}/${CLIENT_NAME}.crt" \
    -storepass "${AGENT_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${AGENT_NAME}/keystore/${AGENT_NAME}.truststore.p12"  && \

# Import Root CA, TLS CA, zuul, eureka, advertisement, auth, message and client certificate into email keystore
keytool \
    -importcert \
    -noprompt \
    -alias "root" \
    -file ca/root-ca.crt \
    -storepass "${EMAIL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EMAIL_NAME}/keystore/${EMAIL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "tls" \
    -file ca/tls-ca.crt \
    -storepass "${EMAIL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EMAIL_NAME}/keystore/${EMAIL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${ORDER_ALIAS}" \
    -file "certs/${ORDER_NAME}/${ORDER_NAME}.crt" \
    -storepass "${EMAIL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EMAIL_NAME}/keystore/${EMAIL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${ZUUL_ALIAS}" \
    -file "certs/${ZUUL_NAME}/${ZUUL_NAME}.crt" \
    -storepass "${EMAIL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EMAIL_NAME}/keystore/${EMAIL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${ADVERT_ALIAS}" \
    -file "certs/${ADVERT_NAME}/${ADVERT_NAME}.crt" \
    -storepass "${EMAIL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EMAIL_NAME}/keystore/${EMAIL_NAME}.truststore.p12"  && \
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
    -alias "${MESSAGE_ALIAS}" \
    -file "certs/${MESSAGE_NAME}/${MESSAGE_NAME}.crt" \
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
    -keystore "certs/${EMAIL_NAME}/keystore/${EMAIL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLIENT_ALIAS}" \
    -file "certs/${CLIENT_NAME}/${CLIENT_NAME}.crt" \
    -storepass "${EMAIL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EMAIL_NAME}/keystore/${EMAIL_NAME}.truststore.p12"  && \


# Import Root CA, TLS CA, zuul, eureka, advertisement, auth, message and client certificate into order keystore
keytool \
    -importcert \
    -noprompt \
    -alias "root" \
    -file ca/root-ca.crt \
    -storepass "${ORDER_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ORDER_NAME}/keystore/${ORDER_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "tls" \
    -file ca/tls-ca.crt \
    -storepass "${ORDER_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ORDER_NAME}/keystore/${ORDER_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${EUREKA_ALIAS}" \
    -file "certs/${EUREKA_NAME}/${EUREKA_NAME}.crt" \
    -storepass "${ORDER_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ORDER_NAME}/keystore/${ORDER_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${ZUUL_ALIAS}" \
    -file "certs/${ZUUL_NAME}/${ZUUL_NAME}.crt" \
    -storepass "${ORDER_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ORDER_NAME}/keystore/${ORDER_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${ADVERT_ALIAS}" \
    -file "certs/${ADVERT_NAME}/${ADVERT_NAME}.crt" \
    -storepass "${ORDER_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ORDER_NAME}/keystore/${ORDER_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${AUTH_ALIAS}" \
    -file "certs/${AUTH_NAME}/${AUTH_NAME}.crt" \
    -storepass "${ORDER_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ORDER_NAME}/keystore/${ORDER_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${MESSAGE_ALIAS}" \
    -file "certs/${MESSAGE_NAME}/${MESSAGE_NAME}.crt" \
    -storepass "${ORDER_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ORDER_NAME}/keystore/${ORDER_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${RMQ_NAME}" \
    -file "certs/${RMQ_NAME}/${RMQ_NAME}.crt" \
    -storepass "${ORDER_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ORDER_NAME}/keystore/${ORDER_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLIENT_ALIAS}" \
    -file "certs/${CLIENT_NAME}/${CLIENT_NAME}.crt" \
    -storepass "${ORDER_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ORDER_NAME}/keystore/${ORDER_NAME}.truststore.p12" || exit
