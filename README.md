# clone-word
clone vocabulary

# Build
./gradlew build

# Docker Build
docker buildx build -t clone-word --platform linux/amd64 .

# Docker Tag
docker tag clone-word hungpham29/clone-word

# Docker Push
docker push hungpham29/clone-word