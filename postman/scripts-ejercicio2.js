// Pruebas del Ejercicio 2. Cada bloque va en Scripts > Post-response de su petición.

// ---- 2 - REST Countries v3.1 descontinuada ----
pm.test("Tras la redirección responde 200", () => pm.response.to.have.status(200));
pm.test("Avisa que la versión está descontinuada", () => {
    const respuesta = pm.response.json();
    pm.expect(respuesta.success).to.eql(false);
    pm.expect(respuesta.errors[0].message).to.include("deprecated");
});

// ---- 2b - GET countries.dev /name/colombia ----
pm.test("El código es 200", () => pm.response.to.have.status(200));
pm.test("Trae Colombia con capital, población y coordenadas", () => {
    const [pais] = pm.response.json();
    pm.expect(pais.name).to.eql("Colombia");
    pm.expect(pais.capital).to.eql("Bogotá");
    pm.expect(pais.population).to.be.a("number");
    pm.expect(pais.latlng).to.have.lengthOf(2);
    pm.environment.set("lat", pais.latlng[0]);
    pm.environment.set("lon", pais.latlng[1]);
});

// ---- 2b - GET Open-Meteo en las coordenadas del país ----
pm.test("El código es 200", () => pm.response.to.have.status(200));
pm.test("Trae temperatura y viento actuales", () => {
    const actual = pm.response.json().current;
    pm.expect(actual.temperature_2m).to.be.a("number");
    pm.expect(actual.wind_speed_10m).to.be.a("number");
});

// ---- 2c - GET countries.dev /name/wakanda ----
pm.test("El país no existe: 404", () => pm.response.to.have.status(404));
pm.test("El cuerpo es texto plano, no JSON", () => {
    pm.expect(pm.response.headers.get("Content-Type")).to.include("text/plain");
    pm.expect(pm.response.text()).to.eql("Country not found");
});
