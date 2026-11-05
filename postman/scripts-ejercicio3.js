// Pruebas del Ejercicio 3. Cada bloque va en Scripts > Post-response de su petición.

// ---- 3a - REST GET countries.dev /alpha/co ----
pm.test("El código es 200", () => pm.response.to.have.status(200));
pm.test("Trae nombre, capital y moneda, y mucho más", () => {
    const pais = pm.response.json();
    pm.expect(pais.name).to.eql("Colombia");
    pm.expect(pais.capital).to.eql("Bogotá");
    pm.expect(pais.currencies[0].code).to.eql("COP");
    pm.expect(Object.keys(pais).length).to.be.above(10);
    pm.environment.set("bytesRest", pm.response.size().body);
});

// ---- 3a - GraphQL country(code: "CO") ----
pm.test("El código es 200", () => pm.response.to.have.status(200));
pm.test("Trae solo los campos pedidos", () => {
    const pais = pm.response.json().data.country;
    pm.expect(pais).to.have.all.keys("name", "capital", "currency", "languages");
    pm.expect(pais.name).to.eql("Colombia");
    pm.expect(pais.currency).to.eql("COP");
});
pm.test("Pesa menos que la respuesta REST", () => {
    const bytesRest = Number(pm.environment.get("bytesRest"));
    pm.expect(pm.response.size().body).to.be.below(bytesRest);
});

// ---- 3b - SOAP FullCountryInfo CO ----
pm.test("El código es 200", () => pm.response.to.have.status(200));
pm.test("Responde en XML", () => pm.expect(pm.response.headers.get("Content-Type")).to.include("text/xml"));
pm.test("Responde un sobre SOAP con Colombia, su capital y su moneda", () => {
    const xml = pm.response.text();
    pm.expect(xml).to.include("<soap:Envelope");
    pm.expect(xml).to.include("<soap:Body>");
    pm.expect(xml).to.include("<m:sName>Colombia</m:sName>");
    pm.expect(xml).to.include("<m:sCapitalCity>Bogota</m:sCapitalCity>");
    pm.expect(xml).to.include("<m:sCurrencyISOCode>COP</m:sCurrencyISOCode>");
});

// ---- 3b - SOAP WSDL ----
pm.test("El código es 200", () => pm.response.to.have.status(200));
pm.test("Publica el contrato con la operación FullCountryInfo", () => {
    const wsdl = pm.response.text();
    pm.expect(wsdl).to.include("definitions");
    pm.expect(wsdl).to.include('operation name="FullCountryInfo"');
});

// ---- 3d - POST webhook pago.aprobado ----
pm.test("webhook.site recibió el aviso: 200", () => pm.response.to.have.status(200));
