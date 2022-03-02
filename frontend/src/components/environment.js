const environment = Object.freeze({
  backend: process.env.BACKEND_HOST || "127.0.0.1",
});


module.exports = environment;

