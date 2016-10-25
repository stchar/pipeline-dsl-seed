

node('master') {
    def flavour = 'sandbox'

    if(tools_ref.equals('master')) {
        flavour = null
    }

    projects=[
        [
            name:'scm',
            jobs:[
                [
                    name:'scm-publisher'+(flavour ? "-${flavour}" : ""),
                    dsl:'scm/dsl/scm-publisher.groovy',
                    pipeline:'scm/pipeline/scm-publisher.groovy',
                ]
            ]
        ],
    ]

    for (prj in projects) {
        // TODO generate only jobs that changed
        // idea is to use gradle as it can analyze dependencies
        stage "Publish ${prj.name} jobs"
        echo "Publishing ${prj.name}'s jobs ${prj.jobs}"
        build(
            job: 'scm-dsl-seeder',
            parameters: [
                [$class: 'TextParameterValue', name: 'jobs_map', value: prj.jobs],
            ]
        )
    }
}
