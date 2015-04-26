<!-- utility functions -->
function createPagesArray(count){
    if (isNaN(count)) return [];
    count = Math.floor(count / 20);
    var pagesArray = []
    for (var i = 0; i < count; i++) {
        pagesArray[i] = i + 1;
    }
    return pagesArray;
}

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
    $(".note-editable").each(function(){
       $(this).html("");
    });
}

function createPlainData() {
    return {
        'success': 'true',
        'errorMessages': [],
        'errorFields': []
    };
}

function validateConfirmations(firstFieldId, secondFieldId, messages, message) {
    var first = document.getElementById(firstFieldId).getElementsByTagName('input')[0].value;
    var second = document.getElementById(secondFieldId).getElementsByTagName('input')[0].value;
    console.log(first + " " + second);
    if (first == second) return;
    var errorFields = [];
    errorFields.push(firstFieldId);
    errorFields.push(secondFieldId);
    addErrorHighlights(errorFields);
    messages.push(message);
}

function generatePagination(offersCount, currentPage, searchWithPageParamEmptyAndLast) {
    var pagesCount = (offersCount / 20) + 1;
    var pages = [];
    for (var index = 1; index <= pagesCount; index++) {
        var page = {pageNumber: index, pageLink: searchWithPageParamEmptyAndLast + index, isActive: index == currentPage};
        pages.push(page);
    }
    return pages;
}

function getPreviousPage(pages, currentPage) {
    var previousPageIndex = currentPage - 1;
    if (previousPageIndex < 1) {
        previousPageIndex = 1;
    }
    return pages[previousPageIndex - 1];
}

function getNextPage(pages, currentPage) {
    var nextPageIndex = currentPage + 1;
    if (nextPageIndex > pages.length) {
        nextPageIndex = pages.length;
    }
    return pages[nextPageIndex - 1];
}