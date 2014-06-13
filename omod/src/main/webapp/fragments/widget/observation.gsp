<%
    config.require("id", "fieldProvider", "field", "concept", "formFieldName")

    def concept = conceptService.getConceptByUuid(config.concept)

    def fragmentOptions = [ label: ui.format(concept) ] << config

    if (concept.getDatatype().isCoded()) {
        def options = []
        concept.getAnswers().each { answer ->
            options.add([label: ui.format(answer.getAnswerConcept()), value: answer.getAnswerConcept().getUuid()])
        }

        fragmentOptions << [ options: options ]
    }
%>

${ ui.includeFragment(config.fieldProvider, "field/" + config.field, fragmentOptions)}