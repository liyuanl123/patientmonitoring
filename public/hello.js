$(document).ready(function() {
    $.ajax({
        url: "http://localhost:8080/api/v1/stats/daily/device:24:04:2019:14:00:00/2020-04-14/2020-04-19"
    }).then(function(data, status, jqxhr) {
       $('.greeting-id').append(data[0].deviceID);
       $('.greeting-content').append(data[0].date);
       console.log(jqxhr, data);
    });
});
