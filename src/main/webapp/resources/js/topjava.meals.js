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
    $.get(context.ajaxUrl + "filter", $('#dateForm').serialize())
        .done(function (data) {
            context.datatableApi.clear().rows.add(data).draw()
        });
}

function resetForm() {
    $('#dateForm').find(':input').val("");
    updateTable();
}
function saveMeal() {
    var formM=$("#dateForm");
    $.ajax({
        type: "POST",
        url: context.ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        if (formM.find('#startDate').val()||formM.find('#startTime').val()||formM.find('#endDate').val()||formM.find('#endTime').val()){
            filter();
        }
        else {
            updateTable();
        }
        successNoty("Saved");
    });
}