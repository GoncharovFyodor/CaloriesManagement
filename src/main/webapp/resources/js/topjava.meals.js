$(function () {
    makeEditable({
        ajaxUrl: "ajax/meals/",
        datatableApi: $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    });
});
function filter() {
    var form=$('#dateForm');
    var startDate=(form.find('#startDate').val());
    var startTime=(form.find('#startTime').val());
    var endDate=(form.find('#endDate').val());
    var endTime=(form.find('#endTime').val());
    $.ajax({
        type:"GET",
        url:"ajax/meals/filter",
        data:form.serialize()
    });
}

function resetForm() {
    $('#dateForm').find(':input').val("");
    filter();
}