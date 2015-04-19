<!-- utility functions -->
function FieldsByIDsManipulator(fieldIds) {
    this.fieldIds = fieldIds;
}

FieldsByIDsManipulator.prototype.removeClasses = function (classes) {
    var field;
    for (var fieldIndex = 0; fieldIndex < this.fieldIds.length; fieldIndex++) {
        field = document.getElementById(this.fieldIds[fieldIndex]);
        for (var classIndex = 0; classIndex < classes.length; classIndex++) {
            field.classList.remove(classes[classIndex]);
        }
    }
};

FieldsByIDsManipulator.prototype.addClasses = function (classes) {
    var field;
    for (var fieldIndex = 0; fieldIndex < this.fieldIds.length; fieldIndex++) {
        field = document.getElementById(this.fieldIds[fieldIndex]);
        for (var classIndex = 0; classIndex < classes.length; classIndex++) {
            field.classList.add(classes[classIndex]);
        }
    }
};

function getFieldsByIds(fieldIds) {
    return new FieldsByIDsManipulator(fieldIds);
}

function showFailMessage(title, message) {
    document.getElementById("response-message").innerHTML='<div class="alert alert-danger alert-dismissible fade in" role="alert">' +
    '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true"><span class="typcn typcn-delete"></span></span></button>' +
    '<strong>' + title + ' </strong> ' + message + '</div>';
}

function showSuccessMessage(title, message) {
    document.getElementById("response-message").innerHTML='<div class="alert alert-success alert-dismissible fade in" role="alert">' +
    '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true"><span class="typcn typcn-delete"></span></span></button>' +
    '<strong>' + title + ' </strong> ' + message + '</div>';
}

function removeErrorHighlights(fieldIds) {
    getFieldsByIds(fieldIds).removeClasses(['has-error']);
}

function addErrorHighlights(fieldIds) {
    getFieldsByIds(fieldIds).addClasses(['has-error']);
}

// All field form-groups should have id-s in the following format: cv-*name*-field.
function convertFieldNamesToFieldIds(fieldNames) {
    var fieldName, index, fieldId;
    var fieldIds = [];
    for (index = 0; index < fieldNames.length; index += 1) {
        fieldName = fieldNames[index];
        fieldId = 'cv-' + fieldName.toLowerCase() + "-field";
        fieldIds[index] = fieldId;
    }
    return fieldIds;
}

function addBothIfOneExists(array, first, second) {
    if (array.indexOf(first) > -1 && array.indexOf(second) == -1) {
        array[array.length] = second;
    }

    if (array.indexOf(second) > -1 && array.indexOf(first) == -1) {
        array[array.length] = first;
    }
}

function hideForm(className){
    $("."+className).hide();
}

function createErrorMessagesHtml(errorMessages) {
    var message = "<ul class='list-unstyled'>";
    for (var index = 0; index < errorMessages.length; index++) {
        message += "<li>" + errorMessages[index] + "</li>";
    }
    message += "</ul>";
    return message;
}

function emptyAllInputs() {
    $(":input").each(function() {
        $(this).val("");
    });
}