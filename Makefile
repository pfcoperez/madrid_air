docker:
	docker build --build-arg eshost=${ESHOST}  --build-arg esuser=${ESUSER} --build-arg espass=${ESPASS} -t update_measurements ./
