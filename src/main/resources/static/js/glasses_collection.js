const glassesmen_products = [
    {id:1,name:"Duncan", brand:"Ray-Ban", price:"15.95", img:"img/k2.png", img_ct:"img/k1.png"},
    {id:2,name:"Simon", brand:"Michael Kors", price:"15.95", img:"img/k4.jpg", img_ct:"img/k3.jpg"},
    {id:3,name:"Omar", brand:"Ray-Ban", price:"15.95", img:"img/k6 .png", img_ct:"img/k5 .png"},
    {id:4,name:"Alvin", brand:"Michael Kors", price:"19.95", img:"img/k8.jpg", img_ct:"img/k7.jpg"},
    {id:5,name:"Esme", brand:"Michael Kors", price:"12.95", img:"img/k10.jpg", img_ct:"img/k9.jpg"},
    {id:6,name:"Oliver", brand:"Michael Kors", price:"12.95", img:"img/k12.jpg", img_ct:"img/k11.jpg"},
    {id:7,name:"Crane", brand:"Ray-Ban", price:"19.95", img:"img/k14.png", img_ct:"img/k13.png"},
    {id:8,name:"Santiago", brand:"Ray-Ban", price:"19.95", img:"img/k10.jpg", img_ct:"img/k9.jpg"},
    {id:9,name:"Carmel", brand:"Ray-Ban", price:"19.95", img:"img/kn2.jpg", img_ct:"img/kn1.jpg"},
    {id:10,name:"Drew", brand:"Michael Kors", price:"19.95", img:"img/kn4.jpg", img_ct:"img/kn3.jpg"},
    {id:11,name:"Noor", brand:"Ray-Ban", price:"19.95", img:"img/kn6.jpg", img_ct:"img/kn5.jpg"},
    {id:12,name:"Tilley", brand:"Ray-Ban", price:"19.95", img:"img/k8.jpg", img_ct:"img/kn7.jpg"},
    {id:13,name:"Annika", brand:"Ray-Ban", price:"12.95", img:"img/kn10.jpg", img_ct:"img/kn9.jpg"},
    {id:14,name:"Cowley", brand:"Michael Kors", price:"12.95", img:"img/kn12.jpg", img_ct:"img/kn11.jpg"},
    {id:15,name:"Braden", brand:"Ray-Ban", price:"15.95", img:"img/kn14.png", img_ct:"img/kn13.png"},
    {id:16,name:"Boggs", brand:"Ray-Ban", price:"15.95", img:"img/kn10.jpg", img_ct:"img/kn9.jpg"},
    {id:17,name:"Rosie", brand:"Michael Kors", price:"19.95", img:"img/kn12.jpg", img_ct:"img/kn11.jpg"},
    {id:18,name:"Fiona", brand:"Ray-Ban", price:"19.95", img:"img/kn14.png", img_ct:"img/kn13.png"},
    {id:19,name:"Merril",brand:"Ray-Ban", price:"19.95", img:"https://assets.glasses.com/is/image/Glasses/805289126577__001.png?impolicy=GL_g-thumbnail-plp", img_ct:"https://assets.glasses.com/is/image/Glasses/805289126577__002.png?impolicy=GL_g-thumbnail-plp"},
    {id:20,name:"Gillian",brand:"Coach", price:"19.95", img:"https://assets.glasses.com/is/image/Glasses/8056597720670__001.png?impolicy=GL_g-thumbnail-plp", img_ct:"https://assets.glasses.com/is/image/Glasses/8056597720670__002.png?impolicy=GL_g-thumbnail-plp"},
    {id:21,name:"Gideon",brand:"Ray-Ban", price:"15.95", img:"https://assets.glasses.com/is/image/Glasses/725125385503__001.png?impolicy=GL_g-thumbnail-plp", img_ct:"https://assets.glasses.com/is/image/Glasses/725125385503__002.png?impolicy=GL_g-thumbnail-plp"},
    {id:22,name:"Tierney",brand:"Oakley", price:"15.95", img:"https://assets.glasses.com/is/image/Glasses/888392581211__001.png?impolicy=GL_g-thumbnail-plp", img_ct:"https://assets.glasses.com/is/image/Glasses/888392581211__002.png?impolicy=GL_g-thumbnail-plp"},
    {id:23,name:"Bas",brand:"Persol", price:"12.95", img:"img/img_sunglasses/sk10.webp", img_ct:"img/img_sunglasses/sk9.webp"},
    {id:24,name:"Tilley",brand:"Ray-Ban", price:"12.95", img:"img/img_sunglasses/sk12.webp", img_ct:"img/img_sunglasses/sk11.webp"},
    {id:25,name:"Arti",brand:"Arnette", price:"19.95", img:"img/img_sunglasses/sk14.webp", img_ct:"img/img_sunglasses/sk13.webp"},
    {id:26,name:"Yoojin",brand:"Ray-Ban", price:"15.95", img:"img/img_sunglasses/sk16.webp", img_ct:"img/img_sunglasses/sk15.webp"},
    {id:27,name:"Tate",brand:"Persol", price:"19.95", img:"img/img_sunglasses/sk18.webp", img_ct:"img/img_sunglasses/sk17.webp"},
    {id:28,name:"Ellington",brand:"Ray-Ban", price:"19.95", img:"img/img_sunglasses/sk20.webp", img_ct:"https://assets.glasses.com/is/image/Glasses/888392594426__002.png?impolicy=GL_g-thumbnail-plp"},
    {id:29,name:"Nancy",brand:"Ray-Ban", price:"19.95", img:"img/img_sunglasses/skn2.webp", img_ct:"img/img_sunglasses/skn1.webp"},
    {id:30,name:"Vela",brand:"Ray-Ban", price:"19.95", img:"img/img_sunglasses/skn4.webp", img_ct:"img/img_sunglasses/skn3.webp"},
    {id:31,name:"Aubrey",brand:"Michael Kors", price:"25.95", img:"img/img_sunglasses/skn6.webp", img_ct:"img/img_sunglasses/skn5.webp"},
    {id:32,name:"Marisela",brand:"Prada", price:"25.95", img:"img/img_sunglasses/skn8.webp", img_ct:"img/img_sunglasses/skn7.webp"},
    {id:33,name:"Ada",brand:"Oakley", price:"12.95", img:"https://assets.glasses.com/is/image/Glasses/8056597766067__001.png?impolicy=GL_g-thumbnail-plp", img_ct:"https://assets.glasses.com/is/image/Glasses/8056597766067__002.png?impolicy=GL_g-thumbnail-plp"},
    {id:34,name:"Sonia",brand:"Ray-Ban", price:"12.95", img:"img/img_sunglasses/skn12.webp", img_ct:"img/img_sunglasses/skn11.webp"},
    {id:35,name:"Latrell",brand:"Michael Kors", price:"19.95", img:"img/img_sunglasses/skn14.webp", img_ct:"img/img_sunglasses/skn13.webp"},
    {id:36,name:"Leta",brand:"Ray-Ban", price:"15.95", img:"img/img_sunglasses/skn16.webp", img_ct:"img/img_sunglasses/skn15.webp"},
    {id:37,name:"Zora",brand:"Oakley", price:"15.95", img:"https://assets.glasses.com/is/image/Glasses/8056597768788__001.png?impolicy=GL_g-thumbnail-plp", img_ct:"https://assets.glasses.com/is/image/Glasses/8056597768788__002.png?impolicy=GL_g-thumbnail-plp"},
    {id:38,name:"Aurelia",brand:"Coach", price:"19.95", img:"img/img_sunglasses/skn20.webp", img_ct:"img/img_sunglasses/sk19.webp"},

]
// const Sunglasses_product = [
//     {id:1,name:"Merril",brand:"Ray-Ban", price:"19.95", img:"img/img_sunglasses/sk2.avif", img_ct:"img/img_sunglasses/sk1.avif"},
//     {id:2,name:"Gillian",brand:"Coach", price:"19.95", img:"img/img_sunglasses/sk4.avif", img_ct:"img/img_sunglasses/sk3.avif"},
//     {id:3,name:"Gideon",brand:"Ray-Ban", price:"15.95", img:"img/img_sunglasses/sk6.avif", img_ct:"img/img_sunglasses/sk5.avif"},
//     {id:4,name:"Tierney",brand:"Oakley", price:"15.95", img:"img/img_sunglasses/sk8.avif", img_ct:"img/img_sunglasses/sk7.avif"},
//     {id:5,name:"Bas",brand:"Persol", price:"12.95", img:"img/img_sunglasses/sk10.webp", img_ct:"img/img_sunglasses/sk9.webp"},
//     {id:6,name:"Tilley",brand:"Ray-Ban", price:"12.95", img:"img/img_sunglasses/sk12.webp", img_ct:"img/img_sunglasses/sk11.webp"},
//     {id:7,name:"Arti",brand:"Arnette", price:"19.95", img:"img/img_sunglasses/sk14.webp", img_ct:"img/img_sunglasses/sk13.webp"},
//     {id:8,name:"Yoojin",brand:"Ray-Ban", price:"15.95", img:"img/img_sunglasses/sk16.webp", img_ct:"img/img_sunglasses/sk15.webp"},
//     {id:9,name:"Tate",brand:"Persol", price:"19.95", img:"img/img_sunglasses/sk18.webp", img_ct:"img/img_sunglasses/sk17.webp"},

//     {id:10,name:"Nancy",brand:"Ray-Ban", price:"19.95", img:"img/img_sunglasses/skn2.webp", img_ct:"img/img_sunglasses/skn1.webp"},
//     {id:11,name:"Vela",brand:"Ray-Ban", price:"19.95", img:"img/img_sunglasses/skn4.webp", img_ct:"img/img_sunglasses/skn3.webp"},
//     {id:12,name:"Aubrey",brand:"Michael Kors", price:"25.95", img:"img/img_sunglasses/skn6.webp", img_ct:"img/img_sunglasses/skn5.webp"},
//     {id:13,name:"Marisela",brand:"Prada", price:"25.95", img:"img/img_sunglasses/skn8.webp", img_ct:"img/img_sunglasses/skn7.webp"},
//     {id:14,name:"Ada",brand:"Oakley", price:"12.95", img:"img/img_sunglasses/skn10.avif", img_ct:"img/img_sunglasses/skn9.avif"},
//     {id:15,name:"Sonia",brand:"Ray-Ban", price:"12.95", img:"img/img_sunglasses/skn12.webp", img_ct:"img/img_sunglasses/skn11.webp"},
//     {id:16,name:"Latrell",brand:"Michael Kors", price:"19.95", img:"img/img_sunglasses/skn14.webp", img_ct:"img/img_sunglasses/skn13.webp"},
//     {id:17,name:"Leta",brand:"Ray-Ban", price:"15.95", img:"img/img_sunglasses/skn16.webp", img_ct:"img/img_sunglasses/skn15.webp"},
//     {id:18,name:"Zora",brand:"Oakley", price:"15.95", img:"img/img_sunglasses/skn18.avif", img_ct:"img/img_sunglasses/skn17.avif"},
//     {id:19,name:"Aurelia",brand:"Coach", price:"19.95", img:"img/img_sunglasses/skn20.webp", img_ct:"img/img_sunglasses/sk19.webp"},
//     {id:20,name:"Esperanza",brand:"Ray-Ban", price:"19.95", img:"img/img_sunglasses/sk20.webp", img_ct:"img/img_sunglasses/sk10.avif"},
// ]
function saveNameProduct(name){
    saveLocalStorage(name, 'NameProduct');
}
function saveLocalStorage(ob, key){
    var str = JSON.stringify(ob);
    localStorage.setItem(key,str);
}
function getLocalStorage(key){
    if(localStorage.getItem(key)){
        var str = localStorage.getItem(key);
        var ob = JSON.parse(str)
        return ob;
    }
    return undefined;
}
