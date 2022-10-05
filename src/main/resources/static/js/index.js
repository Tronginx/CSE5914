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
    var infoLeft1 = document.getElementById('landMarkTable');
    if (infoLeft1 != null){
        infoLeft1.remove();
    }
    var infoLeft2 = document.getElementById('detailTable');
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
        alert("请选择文件");
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
                //data[i].name是地名，data[i].locations是array， locations(0) is longitude, (1)is latitude
                console.log(result['data']);
                alert("Successfully uploaded!");
                $("#div_show_img").html("<img id='input_img' src='" + result + "'>");
                $("#imgPath").attr("value", result);
                $("#div_upload").removeClass("show");
                infoArray = result['data'];
                buildLandMarkTable(infoArray);
                buildDetailTable(infoArray);

                function buildLandMarkTable(data) {
                    let infoTable = document.createElement('table');
                    let infoHead = document.createElement('thead');
                    let infoBody = document.createElement('tbody');
                    infoTable.appendChild(infoHead);
                    infoTable.appendChild(infoBody);
                    infoTable.setAttribute('id', 'landMarkTable');
                    document.getElementById('Placeholder1').appendChild(infoTable);
                    infoTable.style.width = '100%';
                    infoTable.style.border = '1px solid black';

                    let headings = `<tr><th>Check</th><th>Name</th><th>Location</th></tr>`
                    infoHead.innerHTML += headings;

                    for (let i = 0; i < result['data'][0].length; i++){
                        let row = `<tr><td><input type='checkbox'></td>
                                       <td>${result['data'][0][i]['name']}</td>
                                       <td>${result['data'][0][i]['locations']}</td></tr>`
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

                    for (let i = 0; i < result['data'][1].length; i++){
                        let row = `<tr><td><input type='checkbox'></td>
                                       <td>${result['data'][1][i]['name']}</td>
                                       <td>${result['data'][1][i]['confidence']}</td><td>`
                        for (let j = 0; j < result['data'][1][i]['vertex'].length; j++) {
                            row += `[${result['data'][1][i]['vertex'][j]}, ${result['data'][1][i]['vertex'][j+1]}] `;
                        }
                        row += '</td></tr>';
                        infoBody.innerHTML += row
                    }
                }
            },
            error: function (data) {
                alert("系统错误");
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

    function buildTable(data) {
        let historyTable = document.getElementById('historyTable')
        let tbody = document.getElementById('historyTbody');
        tbody.innerHTML = "";
        for (let i = 0; i < data.length; i++) {
            let row = `<tr>
							<td>${data[i].location}</td>
					  </tr>`
            tbody.innerHTML += row
        }
        historyTable.appendChild(tbody);
    }
}
