FROM node:17.6 as builder
WORKDIR /app
COPY package*.json /app/
RUN npm install
COPY . .
RUN rm -rf build
RUN npm run build

FROM node:17.6  as availability-test
WORKDIR /app

COPY --chown=node:node --from=builder /app/build /app
COPY --from=builder /app/entrypoint.sh /app/entrypoint.sh
COPY --from=builder /app/wait /app/wait

RUN npm install -g serve

CMD ["/app/entrypoint.sh"]
