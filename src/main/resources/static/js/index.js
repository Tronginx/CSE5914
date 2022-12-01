function readURL(input) {
    console.log("reached here");
    if (input.files && input.files[0]) {
        let readFileAsync = (file, current) => new Promise((resolve) => {
            var img = document.getElementById(current);
            const reader = new FileReader()
            reader.readAsDataURL(file)
            reader.onload = () => {
                img.src = reader.result;
            }
        })
        for (let i = 0; i < input.files.length; i++){
            let current = "display-img" + (i+1).toString();
            readFileAsync(input.files[i], current);
        }
        console.log("finished reading");
    }
}

//上传文件
var submitting = false;
function uploadFile() {
    if(submitting){
        return;
    }
    submitting = true;
    // detect any table left before uploading new one
    let infoLeft1 = document.getElementById('bestGuessTable');
    if (infoLeft1 != null){
        infoLeft1.remove();
    }
    //formData里面存储的数据形式，一对key/value组成一条数据，key是唯一的，一个key可能对应多个value
    var myform = new FormData();
    // 此时可以调用append()方法来添加数据
//     myform.append('file', $("#img")[0].files[0]);
    for (let i = 0; i < $("#img")[0].files.length; i++){
        myform.append('file', $("#img")[0].files[i]);
        console.log("myform.appending...");
    }
    //验证不为空
    var file = $("#img")[0].files[0];
    if (file == null) {
        alert("Please choose file you want to recognize");
        return false;
    }
    else {
        $.ajax({
            url: "/upload",
            type: "POST",
            data: myform,
            async: true,
            contentType: false,
            processData: false,
            success: function (result) {// result 是传回来的json
                //TODO: NOTE :result.data是一个 Thing list， 用array接收
                console.log(result['data'])
                submitting = false;
                bestGuesses = result['data']
                buildBestGuessTable(bestGuesses);
                // document.getElementById('Placeholder1').innerHTML="";
                // //data[i].name是地名，data[i].locations是array， locations(0) is longitude, (1)is latitude
                // console.log(result['data']);
                // alert("Successfully uploaded!");
                // $("#div_show_img").html("<img id='input_img' src='" + result + "'>");
                // $("#imgPath").attr("value", result);
                // $("#div_upload").removeClass("show");
                // landmarks = result['data'][0];
                // details = result['data'][1];
                // texts = result['data'][2];
                // translations = result['data'][3];
                // labels = result['data'][4];
                // resources = result['data'][5];
                // buildLandMarkTable(landmarks);
                // buildTextTable(texts, translations);
                // buildDetailTable(details);
                // buildLabelTable(labels);
                // buildResourceTable(resources);
                // newMap(landmarks, resources);
                // let img = document.getElementById("display-img");
                // detailGraph(img, details);
                // submitting = false;
            },
            error: function (data) {
                alert("Please upload image only under 50MB(.png/.jpeg/.webp).");
                // alert("系统错误");
                submitting = false;
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
            buildHistoryTable(HistoryArray)
            console.log(HistoryArray)
        }
    })
}

function searchHistory() {
    let searchResult = []
    let locationName = document.getElementById('locationName').value
    $.ajax({
        method: 'GET',
        url: '/searchHistory/location/' + locationName,
        success: function (response) {
            searchResult = response.data
            buildSearchTable(searchResult)
            console.log(searchResult)
        }
    })
}
