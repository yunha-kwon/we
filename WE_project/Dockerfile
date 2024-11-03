# Dockerfile
# Step 1: Build the react application
FROM node:18-alpine

WORKDIR /src
COPY package.json ./

RUN npm install

COPY . ./

EXPOSE 5173
CMD ["npm", "run", "dev"]

