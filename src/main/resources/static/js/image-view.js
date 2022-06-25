function editImage(input) {

    let divImage = document.getElementById("divImage");
    divImage.innerHTML = '';

    let reader = new FileReader();
    reader.readAsDataURL(input.files[0]);

    reader.onload = function () {
       let img = document.createElement("img");
        img.setAttribute("src", reader.result);
        img.setAttribute("class",'ava');
        divImage.appendChild(img);

    }
}
