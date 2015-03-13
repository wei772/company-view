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

function removeErrorHighlights(fieldIds) {
    'use strict';
    getFieldsByIds(fieldIds).removeClasses(['has-error']);
}

function addErrorHighlights(fieldIds) {
    'use strict';
    getFieldsByIds(fieldIds).addClasses(['has-error']);
}