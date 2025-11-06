#FROM ubuntu:latest
#LABEL authors="ioana"
#
#ENTRYPOINT ["top", "-b"]
FROM node:20-alpine

WORKDIR /app

COPY package*.json ./
RUN npm install

# Nu mai copiem tot codul aici (volumul îl va monta)
EXPOSE 3000

# Setăm Vite să asculte pe toate interfețele
CMD ["npm", "run", "dev", "--", "--host"]
