DES Decryptor
-------------

After making jar file:

java -jar des.jar "/data.json" "/" "Secret password used for encryption ;)" 19 ".htaccess"

This will parse data.json file and with data from it will download .htaccess from given ftp servers.

Sample of json:
[
    {
        "id" : 1,
        "ftp_host": "example.com",
        "ftp_login" : "user",
        "ftp_pass": "password",
        "ftp_directory": "/",
        "port" : 21
    },
    {
        "id" : 2,
        "ftp_host": "example.com",
        "ftp_login" : "user",
        "ftp_pass": "password",
        "ftp_directory": "/",
        "port" : 21

    }
]