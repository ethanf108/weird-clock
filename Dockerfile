FROM clojure:latest as clojure-build
WORKDIR /build
COPY deps.edn .
COPY src/ src/
RUN clojure -M -m cljs.main --optimizations advanced -c weird-clock.main

FROM galenguyer/nginx
WORKDIR /web
COPY --from=clojure-build /build/out ./out/
COPY index.html .
COPY nginx.conf /etc/nginx/nginx.conf
