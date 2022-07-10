import {Visibility} from "./gryffinium.uml.js";

let selectedElement = null;
let selectedMethod = null;
let selectedMethodRow = null;

let containerBody = null;

export function generateModifierInterface(canvas, header, body, element, send, update = false) {

    if (element === undefined) {
        canvas.hide()
        return;
    }
    if (!update) {
        selectedElement = null;
        selectedMethod = null;
        selectedMethodRow = null;

        containerBody = null;
    }

    canvas.show()

    header.innerHTML = "";
    body.innerHTML = "";


    switch (element.getType()) {
        case 'ENUM':
            generateValuesInterface(body, element, send);
        case 'CLASS':
        case 'INTERFACE':
            generateAttributesInterface(body, element, send);
            generateMethodsInterface(body, element, send);
            containerBody = body;
            generateParametersInterface(selectedElement, selectedMethod, send);
            generateEntityHeader(header, element, send);
            break;
        case 'BINARY_ASSOCIATION':
        case 'AGGREGATION':
        case 'COMPOSITION':
            generateLinkHeader(header, element, send);
            generateBinaryAssociationSettingsInterface(body, element, send)
            generateRoleInterface(body, element, element.get('roleSource'), send);
            generateRoleInterface(body, element, element.get('roleTarget'), send);
            break;

    }


}

export function generateRoleInterface(body, element, role, send, update = false) {
    let container = null;
    if (update) {
        container = body;
        container.innerHTML = "";
    } else {
        container = document.createElement("div");
        container.className = "col-sm-auto";
        container.id = "container-role-" + role.getId();
        body.append(container);
    }

    let table = document.createElement("table");
    container.append(table);
    table.className = "table table-bordered";

    let thead = document.createElement("thead");
    table.append(thead);

    let titles = ["Role"]
    thead.append(generateTableHeader(titles));

    let tbody = document.createElement("tbody");
    table.append(tbody);

    let row = document.createElement("tr");
    table.append(row);
    row.append(generateNameInterface(undefined, role, false, function (input) {
        send({
            targetName: role.getId() === "Target" ? input.value : undefined,
            sourceName: role.getId() === "Source" ? input.value : undefined,
            id: element.getId(),
        }, element.getType(), 'UpdateCommand')
    }));

    let row2 = document.createElement("tr");
    table.append(row2);
    let td = document.createElement("td");
    row2.append(td);
    td.append(generateMutliplictyInterface(role, function(input){
        send({
            multiplicityTarget: role.getId() === "Target" ? input.value : undefined,
            multiplicitySource: role.getId() === "Source" ? input.value : undefined,
            id: element.getId(),
        }, element.getType(), 'UpdateCommand')
    }));

}

export function generateMutliplictyInterface(target, onblur){
    let input = document.createElement("input");
    input.className = "form-control form-control-sm form-control-plaintext input-sm text-center";
    input.value = target.multiplicity;
    input.addEventListener('keypress', function (e) {
      if(e.code === "Enter"){
          input.blur();
      }
    })
    input.setAttribute("list", document.getElementById("multiplicity-list").id);
    input.placeholder = "Enter multiplicity";
    input.addEventListener("blur", function () {
        onblur(input);
    });

    return input;
}

export function generateBinaryAssociationSettingsInterface(body, element, send, update = false) {
    let container = null;
    if (update) {
        container = body;
        container.innerHTML = "";
    } else {
        container = document.createElement("div");
        container.className = "col-sm-auto";
        container.id = "container-settings"
        body.append(container);
    }

    let table = document.createElement("table");
    container.append(table);
    table.className = "table table-bordered";

    let thead = document.createElement("thead");
    table.append(thead);

    let titles = ["Settings"];
    thead.append(generateTableHeader(titles));

    let tbody = document.createElement("tbody");
    table.append(tbody);

    let row = document.createElement("tr");
    row.className = "p-5";
    table.append(row);

    let td = document.createElement("td");

    let checkContainer = document.createElement("div");
    checkContainer.className = "form-group text-center";
    row.append(td);
    td.append(checkContainer);

    let check = document.createElement("input");
    check.className = "form-check-input align-middle m-0";
    check.type = "checkbox";
    check.id = "check-" + element.getId();
    check.checked = element.get('isDirected');

    checkContainer.append(check);

    check.onchange = function () {
        send({
            id: element.getId(),
            isDirected: check.checked
        }, element.getType(), 'UpdateCommand');
    }

    let label = document.createElement("label");
    label.className = "form-check-label";
    label.htmlFor = "check-" + element.getId();
    label.innerHTML = "Is directed";

    checkContainer.append(label);

    let row2 = document.createElement("tr");
    row2.className = "p-5";
    table.append(row2);

    let btnContainer = document.createElement("div");
    btnContainer.className = "form-group col-sm-auto ";
    let td2 = document.createElement("td");
    row2.append(td2);
    td2.append(btnContainer);


    let button = generateSwapButton(element, send);
    button.className = "btn btn-primary form-control";
    button.style.backgroundColor = "var(--main-color)";
    btnContainer.append(button);
}

export function generateSwapButton(element, send) {
    let button = document.createElement("button");
    button.className = "btn btn-primary";
    button.innerHTML = "Change direction";
    button.onclick = function () {
        send({
                id: element.getId(),
                sourceId: element.get('target').id,
                targetId: element.get('source').id,
            },
            element.getType(),
            'UpdateCommand');
    }
    return button;
}

export function generateHeaderTitleInterface(element, send) {
    let title = document.createElement('input');
    title.className = 'offcanvas-title form-control form-control-sm form-control-plaintext input-sm text-center p-0';
    title.type = 'text';
    title.addEventListener('keypress', function (e) {
        if(e.code === "Enter"){
            title.blur();
        }
    })
    title.value = element.get('name');
    title.onblur = function () {
        send({
            id: element.getId(),
            name: this.value,
        }, element.getType(), 'UpdateCommand')
    }

    return title
}

export function generateLinkHeader(header, element, send) {
    let title = generateHeaderTitleInterface(element, send);
    header.append(title);
}

export function generateEntityHeader(header, element, send) {
    let title = generateHeaderTitleInterface(element, send);

    let container = document.createElement('div');
    container.className = 'container';
    container.style.margin = '5px 0'

    let row = document.createElement('div');
    container.append(row);
    header.append(container);
    row.className = 'row align-middle';

    let tmp = document.createElement('div');
    tmp.className = 'col-sm-auto';
    tmp.append(title);

    row.append(tmp);

    let visibility = generateVisibilityInterface(element, undefined, function (input) {
        if (input.value !== element.visibility) {
            send({
                    id: element.getId(),
                    visibility: input.value,
                },
                element.getType(),
                'UpdateCommand');
        }
    }).firstChild;

    tmp = document.createElement('div');
    tmp.className = 'col-sm-auto';
    tmp.append(visibility);

    row.append(tmp);


    let abstractContainer = document.createElement('div');
    abstractContainer.className = 'form-check';
    abstractContainer.style.width = "auto";

    let checkbox = generateIsAbstractInterface(element, undefined, function (input) {
        if (input.checked !== element.isAbstract) {
            send({
                    id: element.id,
                    isAbstract: input.checked,
                },
                element.getType(),
                'UpdateCommand');
        }
    }).firstChild

    tmp = document.createElement('div');
    tmp.className = 'col-sm-auto';


    checkbox.className = checkbox.className + ' form-check-input';
    checkbox.id = 'checkbox' + element.id;

    let label = document.createElement('label');
    label.className = 'form-check-label';
    label.setAttribute('for', 'checkbox' + element.id);
    label.innerHTML = 'Is abstract';
    abstractContainer.append(checkbox, label);


    tmp.append(abstractContainer);
    row.append(tmp);

    generateCloseButton(header);
}

export function generateCloseButton(header) {
    //<button type="button" className="btn-close text-reset" data-bs-dismiss="offcanvas" aria-label="Close"></button>
    let closeButton = document.createElement('button');
    closeButton.className = 'btn-close text-reset';
    closeButton.setAttribute('data-bs-dismiss', 'offcanvas');
    closeButton.setAttribute('aria-label', 'Close');
    header.append(closeButton);
}

export function generateAttributesInterface(body, element, send, update = false) {
    let container = null;
    if (update) {
        container = body;
        container.innerHTML = "";
    } else {
        container = document.createElement("div");
        container.className = "col-sm-auto";
        container.id = "container-attributes"
        body.append(container);
    }
    let scroller = document.createElement("div");
    scroller.className = "table-wrapper-scroll-y my-custom-scrollbar";
    container.append(scroller);
    let table = document.createElement("table");
    table.className = "table table-bordered";
    scroller.append(table);

    let thead = document.createElement("thead");
    table.append(thead);

    let titles = ["Attributes", "Types", "Visibility", "is constant", "Is static", "Action"];
    thead.append(generateTableHeader(titles));

    let tbody = document.createElement("tbody");
    table.append(tbody);

    tbody.append(generateAddAttribute(titles, "Add a new attribute", function () {

        send({
                parentId: element.get('id'),
                name: "attribute",
                type: "string",
                visibility: "private",
                isStatic: false,
                isConstant: false,
            },
            'ATTRIBUTE',
            'CreateCommand');
    }));

    for (let attribute of element.get('attributes')) {
        tbody.append(generateAttributeRowInterface(element, attribute, send));
    }
}

export function generateAttributeRowInterface(element, attribute, send) {
    let row = document.createElement("tr");

    // Name
    row.append(generateNameInterface(element, attribute, false, function (input) {
        if (input.value !== attribute.name) {
            send({
                    parentId: element.get('id'),
                    id: attribute.id,
                    name: input.value,
                },
                'ATTRIBUTE',
                'UpdateCommand');
        }
    }));

    // Type
    row.append(generateTypeInterface(element, attribute, false, function (input) {
        if (input.value !== attribute.type) {
            send({
                    parentId: element.get('id'),
                    id: attribute.id,
                    type: input.value,
                },
                'ATTRIBUTE',
                'UpdateCommand');
        }
    }));


    // Visibility
    row.append(generateVisibilityInterface(element, attribute, function (input) {
        if (input.value !== attribute.visibility) {
            send({
                    parentId: element.get('id'),
                    id: attribute.id,
                    visibility: input.value,
                },
                'ATTRIBUTE',
                'UpdateCommand');
        }
    }));

    // Constant
    row.append(generateIsConstantInterface(element, attribute, function (input) {
        if (input.checked !== attribute.isConstant) {
            send({
                    parentId: element.get('id'),
                    id: attribute.id,
                    isConstant: input.checked,
                },
                'ATTRIBUTE',
                'UpdateCommand');
        }
    }));

    // Static
    row.append(generateIsStaticInterface(element, attribute, function (input) {
        if (input.checked !== attribute.isStatic) {
            send({
                    parentId: element.get('id'),
                    id: attribute.id,
                    isStatic: input.checked,
                },
                'ATTRIBUTE',
                'UpdateCommand');
        }
    }));

    // Action
    row.append(generateActionButtonInterface(element, attribute,
        function () {
            let attributes = element.get('attributes');
            let index = attributes.indexOf(attribute);
            if (index > 0) {
                let temp = attributes[index - 1];
                attributes[index - 1] = element.get('attributes')[index];
                attributes[index] = temp;
                element.setAttributes(attributes);
                //generateModifierInterface();
            }
        }, // Up
        function () {
            let attributes = element.get('attributes');
            let index = attributes.indexOf(attribute);
            if (index < attributes.length - 1) {
                let temp = attributes[index + 1];
                attributes[index + 1] = element.get('attributes')[index];
                attributes[index] = temp;
                element.setAttributes(attributes);
                //generateModifierInterface();
            }
        }, // Down
        function () {
            send({
                    parentId: element.get('id'),
                    id: attribute.id,
                },
                'ATTRIBUTE',
                'RemoveCommand');
        }  // Remove
    ));

    return row;
}

export function generateMethodsInterface(body, element, send, update = false) {
    let container = null;
    if (update) {
        container = body;
        container.innerHTML = "";
    } else {
        container = document.createElement("div");
        container.className = "col-sm-auto";
        container.id = "container-methods"
        body.append(container);
    }
    let scroller = document.createElement("div");
    scroller.className = "table-wrapper-scroll-y my-custom-scrollbar";
    container.append(scroller);
    let table = document.createElement("table");
    table.className = "table table-bordered";
    scroller.append(table);

    let head = document.createElement("thead");
    table.append(head);

    let titles = ["Methods", "Return Types", "Visibility", "Is abstract", "Is static", "Action"];
    head.append(generateTableHeader(titles));

    let tbody = document.createElement("tbody");
    table.append(tbody);

    tbody.append(generateAddMethod(titles, element.getType() === 'INTERFACE' ? "Add a new method" : "Add a new constructor or method", element, function (entityType) {

        send({
                parentId: element.get('id'),
                name: entityType === 'METHOD' ? "method" : undefined,
                visibility: "public",
                type: "void",
                isStatic: false,
                isAbstract: false,
            },
            entityType,
            'CreateCommand');
    }));

    for (let constructor of element.get('constructors')) {
        tbody.append(generateConstructorRowInterface(element, constructor, send));
    }
    for (let method of element.get('methods')) {
        tbody.append(generateMethodRowInterface(element, method, send));
    }
}

export function generateMethodRowInterface(element, method, send) {
    let row = document.createElement("tr");

    row.onclick = function () {
        if (selectedMethodRow !== null)
            selectedMethodRow.className = "";
        selectedMethodRow = row;
        selectedMethodRow.className = "table-active";
        selectedElement = element;
        selectedMethod = method;

        generateParametersInterface(element, method, send);
    }

    if (method === selectedMethod) {
        row.className = "table-active";
        selectedMethodRow = row;
    }

    // Name
    row.append(generateNameInterface(element, method, false, function (input) {
        if (input.value !== method.name) {
            send({
                    parentId: element.get('id'),
                    id: method.id,
                    name: input.value,
                },
                'METHOD',
                'UpdateCommand');
        }
    }));

    // Return type
    row.append(generateTypeInterface(element, method, false, function (input) {
        if (input.value !== method.type) {
            send({
                    parentId: element.get('id'),
                    id: method.id,
                    type: input.value,
                },
                'METHOD',
                'UpdateCommand');
        }
    }));

    // Visibility
    row.append(generateVisibilityInterface(element, method, function (input) {
        if (input.value !== method.visibility) {
            send({
                    parentId: element.get('id'),
                    id: method.id,
                    visibility: input.value,
                },
                'METHOD',
                'UpdateCommand');
        }
    }));

    // Abstract
    row.append(generateIsAbstractInterface(element, method, function (input) {
        if (input.checked !== method.isAbstract) {
            send({
                    parentId: element.get('id'),
                    id: method.id,
                    isAbstract: input.checked,
                },
                'METHOD',
                'UpdateCommand');
        }
    }));

    // Static
    row.append(generateIsStaticInterface(element, method, function (input) {
        if (input.checked !== method.isStatic) {
            send({
                    parentId: element.get('id'),
                    id: method.id,
                    isStatic: input.checked,
                },
                'METHOD',
                'UpdateCommand');
        }
    }));

    // Action
    row.append(generateActionButtonInterface(element, method,
        function () {
            let methods = element.get('methods');
            let index = methods.indexOf(method);
            if (index > 0) {
                let temp = methods[index - 1];
                methods[index - 1] = methods[index];
                methods[index] = temp;
                element.setMethods(methods);
                generateModifierInterface();
            }
        }, // Up
        function () {
            let methods = element.get('methods');
            let index = methods.indexOf(method);
            if (index < methods.length - 1) {
                let temp = methods[index + 1];
                methods[index + 1] = methods[index];
                methods[index] = temp;
                element.setMethods(methods);
                generateModifierInterface();
            }
        }, // Down
        function () {
            send({
                    parentId: element.get('id'),
                    id: method.id,
                },
                'METHOD',
                'RemoveCommand');
        }  // Remove
    ));

    return row;
}

export function generateConstructorRowInterface(element, constructor, send) {
    let row = document.createElement("tr");

    row.onclick = function () {
        if (selectedMethodRow !== null)
            selectedMethodRow.className = "";
        selectedMethodRow = row;
        selectedMethodRow.className = "table-active";
        selectedElement = element;
        selectedMethod = constructor;

        generateParametersInterface(element, constructor, send);
    }

    if (constructor === selectedMethod) {
        row.style.backgroundColor = "table-active";
        selectedMethodRow = row;
    }

    // Name
    row.append(generateNameInterface(element, constructor, true));

    row.append(document.createElement("td"));

    // Visibility
    row.append(generateVisibilityInterface(element, constructor, function (input) {
        if (input.value !== constructor.visibility) {
            send({
                    parentId: element.get('id'),
                    id: constructor.id,
                    visibility: input.value,
                },
                'CONSTRUCTOR',
                'UpdateCommand');
        }
    }));

    // Constant
    row.append(document.createElement("td"));

    // Static
    row.append(document.createElement("td"));

    // Action
    row.append(generateActionButtonInterface(element, constructor,
        function () {
            let constructors = element.get('constructors');
            let index = constructors.indexOf(constructor);
            if (index > 0) {
                let temp = constructors[index - 1];
                constructors[index - 1] = constructors[index];
                constructors[index] = temp;
                element.setConstructors(constructors);
                generateModifierInterface();
            }
        }, // Up
        function () {
            let constructors = element.get('constructors');
            let index = constructors.indexOf(constructor);
            if (index < constructors.length - 1) {
                let temp = constructors[index + 1];
                constructors[index + 1] = constructors[index];
                constructors[index] = temp;
                element.setConstructors(constructors);
                generateModifierInterface();
            }
        }, // Down
        function () {
            send({
                    parentId: element.get('id'),
                    id: constructor.id,
                },
                'CONSTRUCTOR',
                'RemoveCommand');
        }  // Remove
    ));

    return row;
}

export function generateValuesInterface(body, element, send, update = false) {
    let container = null;
    if (update) {
        container = body;
        container.innerHTML = "";
    } else {
        container = document.createElement("div");
        container.className = "col-sm-auto";
        container.id = "container-values"
        body.append(container);
    }
    let scroller = document.createElement("div");
    scroller.className = "table-wrapper-scroll-y my-custom-scrollbar";
    container.append(scroller);
    let table = document.createElement("table");
    table.className = "table table-bordered";
    scroller.append(table);

    let head = document.createElement("thead");
    table.append(head);

    let titles = ["Value", "Action"];
    head.append(generateTableHeader(titles));

    let tbody = document.createElement("tbody");
    table.append(tbody);

    tbody.append(generateAddAttribute(titles, "Add a new value", function () {
        send({
                parentId: element.get('id'),
                name: "value",
            },
            'VALUE',
            'CreateCommand');
    }));

    for (let value of element.get('values')) {
        tbody.append(generateValueRowInterface(element, value, send));
    }
}

export function generateValueRowInterface(element, value, send) {
    let row = document.createElement("tr");

    // Name
    row.append(generateNameInterface(element, value, false, function (input) {
        if (input.value !== value.name) {
            send({
                    parentId: element.get('id'),
                    value: input.value,
                    oldValue: value.name,
                },
                'VALUE',
                'UpdateCommand');
        }
    }));


    // Action
    row.append(generateActionButtonInterface(element, value,
        function () {
            let values = element.get('values');
            let index = values.indexOf(value);
            if (index > 0) {
                let temp = values[index - 1];
                values[index - 1] = values[index];
                values[index] = temp;
                element.setValues(values);
                generateModifierInterface();
            }
        }, // Up
        function () {
            let values = element.get('values');
            let index = values.indexOf(value);
            if (index < values.length - 1) {
                let temp = values[index + 1];
                values[index + 1] = values[index];
                values[index] = temp;
                element.setValues(values);
                generateModifierInterface();
            }
        }, // Down
        function () {
            send({
                    parentId: element.get('id'),
                    value: value.name,
                },
                'VALUE',
                'RemoveCommand');
        }  // Remove
    ));

    return row;
}

export function generateParametersInterface(element, method, send) {

    let container = null;
    if (document.getElementById("container-parameters") === null) {
        container = document.createElement("div");
        container.className = "col-sm-auto";
        container.id = "container-parameters"
        container.display = "none";
        containerBody.append(container);


    }
    container = document.getElementById("container-parameters");
    container.innerHTML = "";
    let scroller = document.createElement("div");
    scroller.className = "table-wrapper-scroll-y my-custom-scrollbar";
    container.append(scroller);
    let table = document.createElement("table");
    table.className = "table table-bordered";
    scroller.append(table);

    let head = document.createElement("thead");
    table.append(head);

    let titles = ["Parameter", "Type", "Action"];
    head.append(generateTableHeader(titles));

    let tbody = document.createElement("tbody");
    table.append(tbody);

    tbody.append(generateAddAttribute(titles, "Add a new parameter", function () {
        send({
                parentId: element.get('id'),
                methodId: method.id,
                name: "p",
                type: "String",
            },
            'PARAMETER',
            'CreateCommand');
    }));

    if (method === null) {
        container.style.display = "none";
    } else {
        container.style.display = "block";
    }

    if (method !== null) {
        for (let parameter of method.parameters) {
            tbody.append(generateParameterRowInterface(element, method, parameter, send));
        }
    }
}

export function generateParameterRowInterface(element, method, parameter, send) {
    let row = document.createElement("tr");

    // Name
    row.append(generateNameInterface(element, parameter, false, function (input) {
        if (input.value !== parameter.name) {
            send({
                    parentId: element.get('id'),
                    methodId: method.id,
                    id: parameter.id,
                    name: input.value,
                },
                'PARAMETER',
                'UpdateCommand');
        }
    }));

    row.append(generateTypeInterface(element, parameter, false, function (input) {
        if (input.value !== parameter.type) {
            send({
                    parentId: element.get('id'),
                    methodId: method.id,
                    id: parameter.id,
                    type: input.value,
                },
                'PARAMETER',
                'UpdateCommand');
        }
    }));

    // Action
    row.append(generateActionButtonInterface(element, parameter,
        function () {
            let values = element.get('values');
            let index = values.indexOf(value);
            if (index > 0) {
                let temp = values[index - 1];
                values[index - 1] = values[index];
                values[index] = temp;
                element.setValues(values);
                generateModifierInterface();
            }
        }, // Up
        function () {
            let values = element.get('values');
            let index = values.indexOf(value);
            if (index < values.length - 1) {
                let temp = values[index + 1];
                values[index + 1] = values[index];
                values[index] = temp;
                element.setValues(values);
                generateModifierInterface();
            }
        }, // Down
        function () {
            send({
                    parentId: element.get('id'),
                    methodId: method.id,
                    id: parameter.id,
                },
                'PARAMETER',
                'RemoveCommand');
        }  // Remove
    ));

    return row;
}

export function generateTableHeader(headers) {
    let headerRow = document.createElement("tr");

    for (let title of headers) {
        let columnTitle = document.createElement("th");
        columnTitle.className = "text-center";
        columnTitle.innerHTML = title;
        headerRow.append(columnTitle);
    }

    return headerRow;
}

export function generateNameInterface(element, target, disabled, onblur) {
    let attributesTableBodyRowName = document.createElement("td");
    let inputName = document.createElement("input");
    inputName.disabled = disabled;
    inputName.addEventListener('keypress', function (e) {
        if(e.code === "Enter"){
            inputName.blur();
        }
    })
    inputName.className = "form-control form-control-sm form-control-plaintext input-sm text-center p-0";
    inputName.value = target.name;
    inputName.addEventListener("blur", function () {
        onblur(inputName);
    });
    attributesTableBodyRowName.append(inputName);
    return attributesTableBodyRowName;
}

export function generateTypeInterface(element, target, disabled, onblur) {
    let attributesTableBodyRowType = document.createElement("td");
    let inputType = document.createElement("input");
    inputType.addEventListener('keypress', function (e) {
        if(e.code === "Enter"){
            inputType.blur();
        }
    })
    inputType.disabled = disabled;
    inputType.className = "form-control form-control-sm form-control-plaintext input-sm text-center";
    inputType.value = target.type;
    inputType.setAttribute("list", existingTypes.id);
    inputType.placeholder = "Enter type";
    inputType.addEventListener("blur", function () {
        onblur(inputType);
    });
    attributesTableBodyRowType.append(inputType);

    return attributesTableBodyRowType;
}

export function generateVisibilityInterface(element, target, onchange) {
    let attributesTableBodyRowVisibility = document.createElement("td");
    let inputVisibility = document.createElement("select");

    inputVisibility.addEventListener("change", function () {
        onchange(inputVisibility)
    });


    inputVisibility.className = "form-select input-sm text-center";
    let visbilityOptions = ["public", "private", "protected", "package"];
    for (let option of visbilityOptions) {
        let optionElement = document.createElement("option");
        optionElement.value = option;
        optionElement.innerHTML = option;
        if (target !== undefined && Visibility.getVisibility(option) === target.visibility || target === undefined && Visibility.getVisibility(option) === element.get('visibility')) {
            optionElement.selected = true;
        }
        inputVisibility.append(optionElement);
    }
    attributesTableBodyRowVisibility.append(inputVisibility);

    return attributesTableBodyRowVisibility;
}

export function generateIsConstantInterface(element, target, onchange) {
    let attributesTableBodyRowIsConstant = document.createElement("td");
    let inputIsConstant = document.createElement("input");
    inputIsConstant.className = "form-check-input";
    inputIsConstant.type = "checkbox";
    inputIsConstant.checked = target.isConstant;
    inputIsConstant.addEventListener("change", function () {
        onchange(inputIsConstant);
    });

    attributesTableBodyRowIsConstant.append(inputIsConstant);
    return attributesTableBodyRowIsConstant;
}

export function generateIsStaticInterface(element, target, onchange) {
    let attributesTableBodyRowIsStatic = document.createElement("td");
    let inputIsStatic = document.createElement("input");
    inputIsStatic.className = "form-check-input";
    inputIsStatic.type = "checkbox";
    inputIsStatic.checked = target.isStatic;
    inputIsStatic.addEventListener("change", function () {
        onchange(inputIsStatic);
    });
    attributesTableBodyRowIsStatic.append(inputIsStatic);
    return attributesTableBodyRowIsStatic;
}

export function generateIsAbstractInterface(element, target, onchange) {
    let attributesTableBodyRowIsStatic = document.createElement("td");
    let inputIsAbstract = document.createElement("input");
    inputIsAbstract.className = "form-check-input";
    inputIsAbstract.type = "checkbox";
    if (target === undefined) {
        inputIsAbstract.checked = element.get('isAbstract');
    } else {
        inputIsAbstract.checked = target.isAbstract;
    }
    inputIsAbstract.addEventListener("change", function () {
        onchange(inputIsAbstract);
    });
    attributesTableBodyRowIsStatic.append(inputIsAbstract);
    return attributesTableBodyRowIsStatic;
}

export function generateActionButtonInterface(element, target, up, down, remove) {
    let tdAction = document.createElement("td");


    let buttonUp = document.createElement("button");
    buttonUp.className = "btn btn-secondary btn-circle btn-sm";
    buttonUp.innerHTML = "<i class='fa fa-arrow-up'></i>";
    buttonUp.addEventListener("click", function () {
        up();
    });
    tdAction.append(buttonUp);

    let buttonDown = document.createElement("button");
    buttonDown.className = "btn btn-secondary btn-circle btn-sm";
    buttonDown.innerHTML = "<i class='fa fa-arrow-down'></i>";
    buttonDown.addEventListener("click", function () {
        down();
    });
    tdAction.append(buttonDown);

    let buttonDelete = document.createElement("button");
    buttonDelete.className = "btn btn-danger btn-circle btn-sm";
    buttonDelete.innerHTML = "<i class='fa fa-trash-can'></i>";
    buttonDelete.addEventListener("click", function () {
        remove();
    });
    tdAction.append(buttonDelete);
    return tdAction;
}

export function generateAddAttribute(headers, text, onclick) {
    let appendRow = document.createElement("tr");

    let rowText = document.createElement("td");
    rowText.setAttribute("colspan", (headers.length - 1).toString());
    rowText.innerHTML = text;
    appendRow.append(rowText);

    let rowAction = document.createElement("td");
    appendRow.append(rowAction);
    let buttonAdd = document.createElement("button");
    buttonAdd.className = "btn btn-primary btn-circle btn-sm";
    buttonAdd.innerHTML = "<i class='fa fa-plus'></i>";

    buttonAdd.onclick = function () {
        onclick();

    }
    rowAction.append(buttonAdd);
    return appendRow;
}

export function generateAddMethod(headers, text, element, onclick) {
    let appendRow = document.createElement("tr");

    let rowText = document.createElement("td");
    rowText.setAttribute("colspan", (headers.length - 1).toString());
    rowText.innerHTML = text;
    appendRow.append(rowText);

    let rowAction = document.createElement("td");
    appendRow.append(rowAction);


    let buttonAddMethod = document.createElement("button");
    buttonAddMethod.className = "btn btn-primary btn-circle btn-sm";
    buttonAddMethod.innerHTML = "<i class='fa fa-plus'></i>";

    if (element.getType() !== "INTERFACE") {
        let buttonAddConstructor = document.createElement("button");
        buttonAddConstructor.className = "btn btn-primary btn-circle btn-sm";
        buttonAddConstructor.innerHTML = "<i class='fa-solid fa-hammer'></i>";

        buttonAddConstructor.onclick = function () {
            onclick('CONSTRUCTOR');
        }
        rowAction.append(buttonAddConstructor);
    }
    buttonAddMethod.onclick = function () {
        onclick('METHOD');
    }
    rowAction.append(buttonAddMethod);
    return appendRow;
}