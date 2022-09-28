function getHistory() {
    let HistoryArray = []
    $.ajax({
        method:'GET',
        url:'/getHistory',
        success:function(response){
            HistoryArray = response.data
            buildTable(HistoryArray)
            console.log(HistoryArray)
        }
    })
    function buildTable(data){
        let historyTable = document.getElementById('historyTable')
        for (let i = 0; i < data.length; i++){
            let row = `<tr>
							<td>${data[i].location}</td>
					  </tr>`
            historyTable.innerHTML += row
        }
    }
}