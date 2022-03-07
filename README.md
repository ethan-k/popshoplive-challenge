# Popshoplive backend challenge

## Prerequsites

1. Linux or MacOS 
2. Java 17+

# Local run

```shell
./mvnw clean pacakge 
./mvnw spring-boot:run 
```

## Upload a file
```shell
# Local
curl --location --request POST 'http://localhost:8080/photos' \
    --form 'photo=@"<path_to_file>"' \
    --form 'title="title"' \
    --form 'description="description"'

# Heroku 
curl --location --request POST '<heroku-url>/photos' \
    --form 'photo=@"<path_to_file>"' \
    --form 'title="title"' \
    --form 'description="description"
```

## Delete a file
```shell
# Local
curl --location --request DELETE 'http://localhost:8080/photos/<photo_uuid>'

# Heroku
curl --location --request DELETE '<heroku-url>/photos/<photo_uuid>'
```

## Get Photos 
```shell
# Local
curl --location --request GET 'http://localhost:8080/photos'

# Heroku
curl --location --request GET '<heroku-url>/photos' 
```

## Get photo info
```shell
# Local
curl --location --request GET 'http://localhost:8080/photos/<photo_uuid>'

# Heroku
curl --location --request GET '<heroku-url>/photos/<photo_uuid>'
```


## Deploy to heroku

```shell
heroku create
git push heroku master
```




