// Pruebas del Ejercicio 1. Cada bloque va en Scripts > Post-response de su petición.

// ---- 1a - GET /users ----
pm.test("El código es 200", () => pm.response.to.have.status(200));
pm.test("Responde en menos de 1 s", () => pm.expect(pm.response.responseTime).to.be.below(1000));
pm.test("Devuelve una lista de 10 usuarios", () => {
    const usuarios = pm.response.json();
    pm.expect(usuarios).to.be.an("array").that.has.lengthOf(10);
    pm.expect(usuarios[0]).to.have.property("email");
});

// ---- 1a - GET /users/{{userId}}/posts ----
pm.test("El código es 200", () => pm.response.to.have.status(200));
pm.test("Todos los posts son del usuario pedido", () => {
    const userId = Number(pm.environment.get("userId"));
    const posts = pm.response.json();
    pm.expect(posts).to.have.lengthOf(10);
    posts.forEach(p => pm.expect(p.userId).to.eql(userId));
});

// ---- 1a - GET /posts?userId={{userId}} ----
pm.test("El código es 200", () => pm.response.to.have.status(200));
pm.test("El filtro deja solo los posts del usuario pedido", () => {
    const userId = Number(pm.environment.get("userId"));
    const posts = pm.response.json();
    pm.expect(posts).to.have.lengthOf(10);
    posts.forEach(p => pm.expect(p.userId).to.eql(userId));
});

// ---- 1a - POST /posts ----
pm.test("El código es 201", () => pm.response.to.have.status(201));
pm.test("Indica dónde quedó el recurso", () => pm.response.to.have.header("Location"));
pm.test("Devuelve el post con un id nuevo", () => {
    const post = pm.response.json();
    pm.expect(post.title).to.eql("Musubi Japan");
    pm.expect(post).to.have.property("id");
    pm.environment.set("nuevoId", post.id);
});

// ---- 1c - GET /posts/{{nuevoId}} ----
pm.test("El post creado no existe: 404", () => pm.response.to.have.status(404));

// ---- 1a - PUT /posts/{{postId}} ----
pm.test("El código es 200", () => pm.response.to.have.status(200));
pm.test("Devuelve el post reemplazado", () => {
    const post = pm.response.json();
    pm.expect(post.title).to.eql("Titulo nuevo");
    pm.expect(post.body).to.eql("Cuerpo nuevo");
});

// ---- 1d - PUT sin title ----
pm.test("El post queda sin title", () => pm.expect(pm.response.json()).to.not.have.property("title"));

// ---- 1a - PATCH /posts/{{postId}} ----
pm.test("El código es 200", () => pm.response.to.have.status(200));
pm.test("Cambia el título y conserva el resto", () => {
    const post = pm.response.json();
    pm.expect(post.title).to.eql("Solo cambia el titulo");
    pm.expect(post.body).to.include("quia et suscipit");
});

// ---- 1a - DELETE /posts/{{postId}} ----
pm.test("El código es 200", () => pm.response.to.have.status(200));
pm.test("El cuerpo está vacío", () => pm.expect(pm.response.json()).to.eql({}));

// ---- 1a - GET /posts/9999 ----
pm.test("El código es 404", () => pm.response.to.have.status(404));
