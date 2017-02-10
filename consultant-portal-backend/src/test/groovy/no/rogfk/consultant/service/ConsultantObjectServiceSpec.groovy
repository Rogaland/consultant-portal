package no.rogfk.consultant.service

import no.rogfk.consultant.exception.MissingMandatoryAttribute
import no.rogfk.consultant.model.Consultant
import no.rogfk.consultant.model.ConsultantState
import spock.lang.Specification


class ConsultantObjectServiceSpec extends Specification {
    private consultantObjectService
    private configService
    private passwordService

    void setup() {
        configService = new ConfigService(usernamePrefix: "K%s", consultantBaseContainer: "o=consultant")
        passwordService = new PasswordService()
        consultantObjectService = new ConsultantObjectService(configService: configService, passwordService: passwordService)

    }

    def "Setup Invited Consultant Object"() {
        given:
        def invitedConsultant = new Consultant(mobile: "99999999")

        when:
        consultantObjectService.setupInvitedConsultant(invitedConsultant)
        then:
        invitedConsultant.cn != null
        invitedConsultant.state == ConsultantState.INVITED.name()
        invitedConsultant.inviteTimeStamp != null
        invitedConsultant.dn != null
        invitedConsultant.loginDisabled == true
    }

    def "Setup Invited Consultant Object without mandatory attribute set"() {
        given:
        def invitedConsultant = new Consultant()

        when:
        consultantObjectService.setupInvitedConsultant(invitedConsultant)
        then:
        thrown(MissingMandatoryAttribute)
    }

    def "Setup pending consultant"() {
        given:
        def pendingConsultant = new Consultant(mobile: "99999999", firstName: "Ole", lastName: "Olsen", organization: "ConsultingFirm")

        when:
        consultantObjectService.setupInvitedConsultant(pendingConsultant)
        consultantObjectService.setupPendingConsultant(pendingConsultant)

        then:
        pendingConsultant.loginDisabled == false
        pendingConsultant.state == ConsultantState.PENDING.name()
        pendingConsultant.password != null
    }

    def "Setup pending consultant without mandatory attribute set"() {
        given:
        def pendingConsultant = new Consultant(mobile: "99999999")

        when:
        consultantObjectService.setupInvitedConsultant(pendingConsultant)
        consultantObjectService.setupPendingConsultant(pendingConsultant)

        then:
        thrown(MissingMandatoryAttribute)

    }
}
