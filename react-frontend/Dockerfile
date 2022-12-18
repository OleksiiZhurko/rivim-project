FROM node:14.16.0-alpine
WORKDIR /usr/local/app/react_frontend
ENV PATH /usr/local/app/react_frontend/node_modules/.bin:$PATH
COPY package.json /usr/local/app/react_frontend/package.json
COPY . /usr/local/app/react_frontend
RUN npm install
RUN npm run build

CMD [ "npm", "start" ]