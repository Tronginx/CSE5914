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
                let table = document.createElement('table');
                let thead = document.createElement('thead');
                let tbody = document.createElement('tbody');

                table.appendChild(thead);
                table.appendChild(tbody);
                document.getElementById('Placeholder').appendChild(table);

                table.style.width = '100%';
                table.style.border = '1px solid black';

                // Creating and adding data to first row of the table
                let row_1 = document.createElement('tr');
                let heading_id = document.createElement('th');
                heading_id.innerHTML = "Check";
                let heading_1 = document.createElement('th');
                heading_1.innerHTML = "Name";
                let heading_2 = document.createElement('th');
                heading_2.innerHTML = "Location";

                row_1.appendChild(heading_id);
                row_1.appendChild(heading_1);
                row_1.appendChild(heading_2);
                thead.appendChild(row_1);

                // Creating and adding data to second row of the table
                let row_2 = document.createElement('tr');
                let row_2_data_id = document.createElement('td');
                let checkbox1 = document.createElement('input');
                checkbox1.type = 'checkbox';
                row_2_data_id.appendChild(checkbox1);
                let row_2_data_1 = document.createElement('td');
                row_2_data_1.innerHTML = result['data'][0]['name'];
                let row_2_data_2 = document.createElement('td');
                row_2_data_2.innerHTML = result['data'][0]['locations'];

                row_2.appendChild(row_2_data_id);
                row_2.appendChild(row_2_data_1);
                row_2.appendChild(row_2_data_2);
                tbody.appendChild(row_2);


                // Creating and adding data to third row of the table
                let row_3 = document.createElement('tr');
                let row_3_data_id = document.createElement('td');
                let checkbox2 = document.createElement('input');
                checkbox2.type = 'checkbox';
                row_3_data_id.appendChild(checkbox2);
                let row_3_data_1 = document.createElement('td');
                row_3_data_1.innerHTML = result['data'][1]['name'];
                let row_3_data_2 = document.createElement('td');
                row_3_data_2.innerHTML = result['data'][1]['locations'];

                row_3.appendChild(row_3_data_id);
                row_3.appendChild(row_3_data_1);
                row_3.appendChild(row_3_data_2);
                tbody.appendChild(row_3);

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