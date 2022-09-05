FROM nginx:latest
COPY nginx.conf /etc/nginx/nginx.conf
COPY index.html /web/
COPY /out/ /web/out/
