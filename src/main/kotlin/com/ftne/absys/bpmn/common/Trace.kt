package com.ftne.absys.bpmn.common

//import org.springframework.cloud.sleuth.Tracer
import brave.Tracer
import brave.propagation.TraceContext
import brave.propagation.TraceContextOrSamplingFlags
import mu.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.ExecutionListener
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl
import org.camunda.bpm.engine.impl.util.xml.Element
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
class CamundaTraceConfiguration {
    @Bean
    fun traceListener(
        tracer: Tracer,
    ): ProcessTraceListener {
        return ProcessTraceListener(tracer)
    }
}

@Component
class AddExecutionListenerParseListener(
    private val traceListener: ProcessTraceListener,
): AbstractBpmnParseListener() {

    override fun parseServiceTask(serviceTaskElement: Element?, scope: ScopeImpl?, activity: ActivityImpl?) {
        log.info { "parse service task $serviceTaskElement" }
        activity?.let {
            activity.addListener(ExecutionListener.EVENTNAME_START, traceListener)
        }
    }

    override fun parseReceiveTask(receiveTaskElement: Element?, scope: ScopeImpl?, activity: ActivityImpl?) {
        log.info { "parse receive event: $receiveTaskElement" }
        activity?.let {
            activity.addListener(ExecutionListener.EVENTNAME_START, traceListener)
        }
    }

    override fun parseEndEvent(endEventElement: Element?, scope: ScopeImpl?, activity: ActivityImpl?) {
        log.info { "parse end event $endEventElement" }
        activity?.let {
            activity.addListener(ExecutionListener.EVENTNAME_START, traceListener)
        }
    }

    companion object {
        val log = KotlinLogging.logger {}
    }
}

@Component
class ProgressLoggingSupportParseListenerPlugin(
    @Autowired val addExecutionListenerParseListener: AddExecutionListenerParseListener,
) : AbstractProcessEnginePlugin() {

    override fun preInit(processEngineConfiguration: ProcessEngineConfigurationImpl) {

        // get all existing preParseListeners
        var preParseListeners = processEngineConfiguration.customPreBPMNParseListeners
        if (preParseListeners == null) {

            // if no preParseListener exists, create new list
            preParseListeners = ArrayList()
            processEngineConfiguration.customPreBPMNParseListeners = preParseListeners
        }

        // add new BPMN Parse Listener
        preParseListeners.add(addExecutionListenerParseListener)
    }
}

@Component
class ProcessTraceListener(
    private val tracer: Tracer,
): ExecutionListener {
    override fun notify(execution: DelegateExecution?) {
        tracer.withSpanInScope(null)
//        val traceIdl = tracer.currentSpan()?.context()?.traceId()
        log.info { "ProcessTraceListener notify" }
        val newSpan =
            tracer.nextSpan(TraceContextOrSamplingFlags.create(TraceContext.newBuilder().traceId(2645052838382171117).spanId(2645052838382171117).build()))
                .start()
        // val traceId = tracer.currentSpan()?.context()?.traceIdString()

        tracer.withSpanInScope(newSpan)
        log.info { "ProcessTraceListener notify2" }
//        val span: Span = tracer.nextSpan(
//        ).name("name").start()

        // tracer.nextSpan()
        // tracer.toSpan()

//        val currentSpan = tracer.currentSpan();
//        tracer.currentSpan()?.context()?.traceId()
//        val newSpan = tracer.nextSpan().name("ttt2").start()

        // tracer.newTrace().context().toBuilder().traceId(traceIdl!!)
//        val newSpan =
//            tracer.nextSpan(TraceContextOrSamplingFlags.create(TraceContext.newBuilder().traceId(traceIdl!!).spanId(spanId!!).build()))
//                .start()
       // tracer.newTrace().start()

//        tracer.currentSpanCustomizer().
//        log.info { "ProcessTraceListener outside" }
//
//        try {
//             tracer.withSpanInScope(tracer.newTrace().start()).use { log.info { "ProcessTraceListener inside" } }
//        } finally {
//             newSpan.end()
//        }
        //this.tracer.nextSpan().start()
        // tracer.nextSpan(tracer.nextSpan().start()).start()
        // tracer.nextSpan(tracer.startScopedSpan("123"))
        // val traceId2 = tracer.currentSpan()?.context()?.traceId()
        // val spanId2 = tracer.currentSpan()?.context()?.spanId()
//        log.info { "ProcessTraceListener notify2" }
    }
    companion object {
        val log = KotlinLogging.logger { }
    }
}