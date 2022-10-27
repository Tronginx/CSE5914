function initMap() {
    const myLatlng = { lat: 39.9833, lng: -82.9833 };

    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 10,
        center: myLatlng,
    });
}



function readURL(input) {
    console.log("reached here");
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        var img = document.getElementById("display-img");
        reader.readAsDataURL(input.files[0]);
        reader.onload = () => {
            img.src=reader.result;
        };
    }
}

//上传文件
function uploadFile() {
    // detect any table left before uploading new one
    let infoLeft1 = document.getElementById('landmarkTable');
    if (infoLeft1 != null){
        infoLeft1.remove();
    }
    let infoLeft2 = document.getElementById('detailTable');
    if (infoLeft2 != null){
        infoLeft2.remove();
    }

    //formData里面存储的数据形式，一对key/value组成一条数据，key是唯一的，一个key可能对应多个value
    var myform = new FormData();
    // 此时可以调用append()方法来添加数据
    myform.append('file', $("#img")[0].files[0]);
    //验证不为空
    var file = $("#img")[0].files[0];
    if (file == null) {
        alert("Please choose file you want to recognize");
        return false;
    } else {
        $.ajax({
            url: "/upload",
            type: "POST",
            data: myform,
            async: false,
            contentType: false,
            processData: false,
            success: function (result) {// result 是传回来的json
                //TODO: NOTE :result.data是一个 Thing list， 用array接收
                document.getElementById('Placeholder1').innerHTML="";
                //data[i].name是地名，data[i].locations是array， locations(0) is longitude, (1)is latitude
                console.log(result['data']);
                alert("Successfully uploaded!");
                $("#div_show_img").html("<img id='input_img' src='" + result + "'>");
                $("#imgPath").attr("value", result);
                $("#div_upload").removeClass("show");
                landmarks = result['data'][0];
                details = result['data'][1];
                buildLandMarkTable(landmarks);
                buildDetailTable(details);
                newMap(landmarks); //TODO: change later into average form
                let img = document.getElementById("display-img");
                detailGraph(img, details);

                function newMap(landmarks) {
                    let latitude = 39.9833;
                    let longitude = -82.9833;
                    let sumLatitude = 0;
                    let sumLongitude = 0;
                    let size = landmarks.length
                    if (size != 0){
                        for (let i = 0; i < size; i++){
                            sumLatitude += landmarks[i]['latitude'];
                            sumLongitude += landmarks[i]['longitude'];
                        }
                        latitude = sumLatitude / size;
                        longitude = sumLongitude / size;
                    }
                    const myLatlng = { lat: latitude, lng: longitude};

                    const map = new google.maps.Map(document.getElementById("map"), {
                        zoom: 12,
                        center: myLatlng,
                    });
                }

                // TODO: draw blocks on the picture
                function detailGraph(img, details){
                    let canvas = document.querySelector('#detailPicture');
                    let ctx = canvas.getContext('2d');
                    canvas.width = img.width;
                    canvas.height = img.height;
                    let loadImg = new Image();
                    loadImg.src = img.src;
                    loadImg.onload = function(){
                        ctx.drawImage(img, 0, 0, img.width, img.height);
                        ctx.strokeStyle = 'red';
                        ctx.lineWidth = 1;
                        ctx.fillStyle = 'red'
                        ctx.font = 'bold 20px sans-serif';
                        for (let i = 0; i < details.length; i++){
                            let name = details[i]['name'];
                            // console.log('start drawing');
                            ctx.moveTo(details[i]['vertex'][0]*canvas.width, details[i]['vertex'][1]*canvas.height);
                            ctx.lineTo(details[i]['vertex'][2]*canvas.width, details[i]['vertex'][3]*canvas.height);
                            ctx.lineTo(details[i]['vertex'][4]*canvas.width, details[i]['vertex'][5]*canvas.height);
                            ctx.lineTo(details[i]['vertex'][6]*canvas.width, details[i]['vertex'][7]*canvas.height);
                            ctx.lineTo(details[i]['vertex'][0]*canvas.width, details[i]['vertex'][1]*canvas.height);
                            ctx.fillText(name, details[i]['vertex'][0]*canvas.width*0.8, details[i]['vertex'][1]*canvas.height);
                            ctx.stroke();
                            // console.log('finish drawing');
                        }
                    }


                }

                function buildLandMarkTable(data) {
                    let infoTable = document.createElement('table');
                    let infoHead = document.createElement('thead');
                    let infoBody = document.createElement('tbody');
                    infoTable.appendChild(infoHead);
                    infoTable.appendChild(infoBody);
                    infoTable.setAttribute('id', 'landmarkTable');
                    document.getElementById('Placeholder1').appendChild(infoTable);
                    infoTable.style.width = '100%';
                    infoTable.style.border = '1px solid black';

                    let headings = `<tr><th>Check</th><th>Name</th><th>Latitude</th><th>Longitude</th></tr>`
                    infoHead.innerHTML += headings;

                    for (let i = 0; i < data.length; i++){
                        let row = `<tr><td><input type='checkbox'></td>
                                       <td>${data[i]['name']}</td>
                                       <td>${data[i]['latitude']}</td>
                                       <td>${data[i]['longitude']}</td></tr>`
                        infoBody.innerHTML += row
                    }
                }

                function buildDetailTable(data) {
                    let infoTable = document.createElement('table');
                    let infoHead = document.createElement('thead');
                    let infoBody = document.createElement('tbody');
                    infoTable.appendChild(infoHead);
                    infoTable.appendChild(infoBody);
                    infoTable.setAttribute('id', 'detailTable');
                    document.getElementById('Placeholder2').appendChild(infoTable);
                    infoTable.style.width = '100%';
                    infoTable.style.border = '1px solid black';

                    let headings = `<tr><th>Check</th><th>Name</th><th>Confidence</th><th>Vertex</th></tr>`
                    infoHead.innerHTML += headings;

                    for (let i = 0; i < data.length; i++){
                        let row = `<tr><td><input type='checkbox'></td>
                                       <td>${data[i]['name']}</td>
                                       <td>${data[i]['confidence']}</td><td>`
                        for (let j = 0; j <data[i]['vertex'].length; j++) {
                            row += `[${data[i]['vertex'][j]}, ${data[i]['vertex'][j+1]}] `;
                            j++;
                        }
                        row += '</td></tr>';
                        infoBody.innerHTML += row
                    }
                }
            },
            error: function (data) {
                alert("Please upload image only(.png/.jpeg/.webp)");
            }
        });
    }
}

/*------------------*/
//Get history part
// fetch history data
function getHistory() {
    let HistoryArray = []
    $.ajax({
        method: 'GET',
        url: '/getHistory',
        success: function (response) {
            HistoryArray = response.data
            buildTable(HistoryArray)
            console.log(HistoryArray)
        }
    })

// TODO: consider add 'clear' method to the history table
    function buildTable(data) {
        let historyTable = document.getElementById('historyTable')
        let tbody = document.getElementById('historybody');
        tbody.innerHTML = "";
        for (let i = 0; i < data.length; i++) {
            let row = `<tr>
                            <td><input type='checkbox'></td>
							<td>${data[i].location}</td>
					  </tr>`
            tbody.innerHTML += row
        }
        historyTable.appendChild(tbody);
    }
}

function searchHistory() {
    let searchResult = []
    let locationName = document.getElementById('locationName').value
    $.ajax({
        method: 'GET',
        url: '/searchHistory/location/' + locationName,
        success: function (response) {
            searchResult = response.data
            buildTable(searchResult)
            console.log(searchResult)
        }
    })

    function buildTable(data) {
        let searchTable = document.getElementById('searchTable')
        let tbody = document.getElementById('searchTbody');
        tbody.innerHTML = "";
        if (data.length === 0) {
            let row = `<tr>
							<td>No Data Found</td>
					  </tr>`
            tbody.innerHTML += row
        } else {
            for (let i = 0; i < data.length; i++) {
                let row = `<tr>
							<td>${data[i].latitude}</td>
					  </tr>`
                tbody.innerHTML += row
            }
        }
        searchTable.appendChild(tbody);
    }
}
