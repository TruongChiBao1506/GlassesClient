
let dataGlasses = [
    { id: "G1", src: "./img/a1.jpg", virtualImg: "./img/v1.png", brand: "Armani Exchange", name: "Bamboo wood", color: "Brown", price: 150, description: "Đây là một chiếc kính mắt đẹp và sang trọng của thương hiệu Armani Exchange, với tên gọi Bamboo wood. Kính được thiết kế với chất liệu gỗ tre tự nhiên và màu nâu trầm tinh tế, tạo nên sự kết hợp hoàn hảo giữa sự sang trọng và thân thiện với môi trường. " },
    { id: "G2", src: "./img/a2.jpg", virtualImg: "./img/v2.png", brand: "Arnette", name: "American flag", color: "American flag", price: 150, description: "Sản phẩm mắt kính Arnette American Flag sẽ mang đến cho bạn phong cách thể thao, mạnh mẽ và đầy cá tính. Thiết kế này được trang trí với hình cờ Mỹ, tạo nên sự độc đáo và khác biệt cho chiếc kính của bạn. Với giá 150 đô la, sản phẩm này sẽ không chỉ bảo vệ đôi mắt của bạn mà còn làm bạn nổi bật trong mọi dịp" },
    { id: "G3", src: "./img/a3.jpg", virtualImg: "./img/v3.png", brand: "Burberry", name: "Belt of Hippolyte", color: "Blue", price: 100, description: "Sản phẩm mắt kính thời trang từ thương hiệu Burberry với mẫu Belt of Hippolyte (G3). Với thiết kế sang trọng, mắt kính được làm bằng chất liệu chất lượng cao, đảm bảo độ bền và sự thoải mái khi đeo. Với tông màu xanh đậm đẹp mắt, đây là phụ kiện hoàn hảo để tôn lên phong cách của bạn" },
    { id: "G4", src: "./img/a4.jpg", virtualImg: "./img/v4.png", brand: "Coarch", name: "Cretan Bull", color: "Red", price: 100, description: "Mắt kính thời trang Coarch Cretan Bull màu đỏ là một lựa chọn hoàn hảo cho những người yêu thích phong cách trẻ trung, hiện đại. Thiết kế với họa tiết đầu bò trông rất nổi bật và đầy phong cách." },
    { id: "G5", src: "./img/a5.jpg", virtualImg: "./img/v5.png", brand: "D&G", name: "JOYRIDE", color: "Gold", price: 180, description: "Mắt kính JOYRIDE của thương hiệu D&G là sự kết hợp tuyệt vời giữa phong cách cổ điển và hiện đại. Với gam màu Gold trang nhã, sản phẩm này sẽ giúp bạn thêm phần quý phái và thu hút mọi ánh nhìn" },
    { id: "G6", src: "./img/a6.jpg", virtualImg: "./img/v6.png", brand: "Polo", name: "NATTY ICE", color: "Blue, White", price: 120, description: "Mắt kính Polo Natty Ice với thiết kế màu xanh trắng độc đáo và hiện đại sẽ giúp bạn tự tin hơn trong mỗi bước chân. Sản phẩm này có giá 120 đô la và được làm từ các chất liệu chất lượng cao để đảm bảo tính bền vững và sự thoải mái khi sử dụng." },
    { id: "G7", src: "./img/a7.jpg", virtualImg: "./img/v7.png", brand: "Ralph", name: "TORTOISE", color: "Black, Yellow", price: 120, description: "Sản phẩm mắt kính của thương hiệu Ralph mang tên TORTOISE, với hai màu đen và vàng độc đáo. Với giá 120 đô la, chiếc mắt kính này sẽ mang đến cho bạn sự thoải mái và tự tin khi sử dụng." },
    { id: "G8", src: "./img/a8.jpg", virtualImg: "./img/v8.png", brand: "Polo", name: "NATTY ICE", color: "Red, Black", price: 120, description: "sản phẩm đến từ thương hiệu Polo, được thiết kế với mẫu mã hiện đại và sành điệu. Với tên gọi NATTY ICE và màu sắc chủ đạo là đỏ và đen, chiếc kính này sẽ là một phụ kiện hoàn hảo để bạn thể hiện phong cách cá tính" },
    { id: "G9", src: "./img/a9.jpg", virtualImg: "./img/v9.png", brand: "Coarch", name: "MIDNIGHT VIXEN REMIX", color: "Blue, Black", price: 120, description: "Sản phẩm mắt kính MIDNIGHT VIXEN REMIX của thương hiệu Coarch là một sản phẩm được thiết kế với hai màu chủ đạo là xanh và đen, tạo nên một sự kết hợp hài hòa và sang trọng." }
];

import { Glasses } from "./glasses.js";
import { GlassesList } from "./glassesList.js";

let glassesList = new GlassesList();


const getELE = (id) => {
    return document.getElementById(id);
}


const showGlassesList = () => {
    let divGlassesList = getELE("vglassesList");


    dataGlasses.map((item, index) => {
        let gl = new Glasses(item.id, item.src, item.virtualImg, item.brand, item.name, item.color, item.price, item.description);
        glassesList.addGlasses(gl);
    });

    divGlassesList.innerHTML = glassesList.renderGlasses();
}

showGlassesList();

const tryOnGlasses = (e) => {
    let gID = e.target.getAttribute("data-id");
    let gObject = {};
    for (let value of glassesList.glist) {
        if (value.id == gID) {
            gObject = value;
        }
    }
    showInfo(gObject);
}

window.tryOnGlasses = tryOnGlasses;

const showInfo = (gObject) => {
    let divAvatar = getELE("avatar");
    let divInfo = getELE("glassesInfo");

    divAvatar.innerHTML = `
        <img id="glasses" src="${gObject.virtualImg}">
    `;

    let status = "";
    if (gObject.status) {
        status = "Stocking";
    } else {
        status = "Sold Out";
    }

    divInfo.innerHTML = `
        <h5>${gObject.name} - ${gObject.brand} (${gObject.color})</h5>
        <p class="card-text"> 
            <span class="btn btn-danger btn-sm mr-2">$${gObject.price}</span>
            <span class="text-success">${status}</span>
        </p>
        <p class="card-text"> 
            ${gObject.desc}
        </p>
    `;
    divInfo.style.display = "block";
}
const removeGlasses = (isDisplay) => {
    let glasses = getELE("glasses");
    if (glasses == null) {
        return;
    }
        if (isDisplay) {
            glasses.style.opacity = 0.9;
        } else {
            glasses.style.opacity = 0;
        }
}

window.removeGlasses = removeGlasses;