<!-- utility functions -->
function FieldsByIDsManipulator(fieldIds) {
    'use strict';
    this.fieldIds = fieldIds;
}

FieldsByIDsManipulator.prototype.removeClasses = function (classes) {
    'use strict';
    var fieldIndex, field, classIndex;
    for (fieldIndex = 0; fieldIndex < this.fieldIds.length; fieldIndex += 1) {
        field = document.getElementById(this.fieldIds[fieldIndex]);
        for (classIndex = 0; classIndex < classes.length; classIndex += 1) {
            field.classList.remove(classes[classIndex]);
        }
    }
};

FieldsByIDsManipulator.prototype.addClasses = function (classes) {
    'use strict';
    var fieldIndex, field, classIndex;
    for (fieldIndex = 0; fieldIndex < this.fieldIds.length; fieldIndex += 1) {
        field = document.getElementById(this.fieldIds[fieldIndex]);
        for (classIndex = 0; classIndex < classes.length; classIndex += 1) {
            field.classList.add(classes[classIndex]);
        }
    }
};

function getFieldsByIds(fieldIds) {
    'use strict';
    return new FieldsByIDsManipulator(fieldIds);
}

function showFailMessage(elementId, title, message) {
    document.getElementById(elementId).innerHTML='<div class="alert alert-danger alert-dismissible fade in" role="alert">' +
    '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true"><span class="typcn typcn-delete"></span></span></button>' +
    '<strong>' + title + ' </strong> ' + message + '</div>';
}

function showSuccessMessage(elementId, title, message) {
    document.getElementById(elementId).innerHTML='<div class="alert alert-success alert-dismissible fade in" role="alert">' +
    '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true"><span class="typcn typcn-delete"></span></span></button>' +
    '<strong>' + title + ' </strong> ' + message + '</div>';
}

function removeErrorHighlights(fieldIds) {
    'use strict';
    getFieldsByIds(fieldIds).removeClasses(['has-error']);
}

function addErrorHighlights(fieldIds) {
    'use strict';
    getFieldsByIds(fieldIds).addClasses(['has-error']);
}

// All field form-groups should have id-s in the following format: cv-*name*-field.
function convertFieldNamesToFieldIds(fieldNames) {
    'use strict';
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