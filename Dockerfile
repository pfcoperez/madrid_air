FROM alpine:latest

RUN apk --update add openjdk8-jre
RUN apk --update add curl
RUN apk --update add bash
RUN apk --update add jq
RUN (echo "#!/usr/bin/env sh" && curl -L https://github.com/lihaoyi/Ammonite/releases/download/1.1.2/2.12-1.1.2) > /usr/local/bin/amm && chmod +x /usr/local/bin/amm

ADD es/ /es
ADD extractor.sc /extractor.sc
ADD update_current_day.sh /update_current_day.sh

RUN /extractor.sc --noop true

ARG eshost
ARG esuser
ARG espass

ENV ESHOST $eshost
ENV ESUSER $esuser
ENV ESPASS $espass

CMD ["/update_current_day.sh"]
