FROM seepine/alpine-glibc
WORKDIR /work/
COPY ./build/*-runner /work/application
RUN chmod 775 /work

CMD ["./application"]