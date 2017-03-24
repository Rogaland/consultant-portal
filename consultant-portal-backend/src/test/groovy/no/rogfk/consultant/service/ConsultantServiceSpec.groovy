package no.rogfk.consultant.service

import no.rogfk.consultant.model.Consultant
import no.rogfk.consultant.model.ConsultantState
import org.springframework.ldap.NameNotFoundException
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.support.LdapNameBuilder
import spock.lang.Specification

import javax.naming.Name
import javax.naming.directory.SearchControls

class ConsultantServiceSpec extends Specification {

    private ConsultantService consultantService
    private ConsultantObjectService consultantObjectService
    private SmsNotifySerivce smsNotifySerivce
    private ConfigService configService
    private LdapTemplate ldapTemplate
    private SearchControls searchControls
    private EmailNotifyService emailNotifyService

    void setup() {
        consultantObjectService = Mock(ConsultantObjectService)
        ldapTemplate = Mock(LdapTemplate)
        smsNotifySerivce = Mock(SmsNotifySerivce)
        configService = Mock(ConfigService)
        searchControls = Mock(SearchControls)
        emailNotifyService = Mock(EmailNotifyService)
        consultantService = new ConsultantService(ldapTemplate: ldapTemplate,
                smsNotifySerivce: smsNotifySerivce, searchControls: searchControls,
                consultantObjectService: consultantObjectService, emailNotifyService: emailNotifyService,
                configService: configService)
    }

    def "Invite Consultant and send sms invite"() {
        when:
        def retVal = consultantService.inviteConsultant(new Consultant(dn: "o=rfk"))

        then:
        retVal == true
        1 * consultantObjectService.setupInvitedConsultant(_ as Consultant)
        1 * ldapTemplate.create(_ as Consultant)
        1 * smsNotifySerivce.sendInvite(_ as Consultant)
        1 * ldapTemplate.lookup(_ as Name) >> { throw new NameNotFoundException("test") }
    }

    def "Invite Consultant that already exists"() {
        when:
        def retVal = consultantService.inviteConsultant(new Consultant(dn: "o=rfk"))

        then:
        retVal == false
        1 * consultantObjectService.setupInvitedConsultant(_ as Consultant)
        0 * ldapTemplate.create(_ as Consultant)
        0 * smsNotifySerivce.sendInvite(_ as Consultant)
        1 * ldapTemplate.lookup(_ as Name) >> new Object()
    }

    def "User does exist"() {
        when:
        def retVal = consultantService.exists("")
        then:
        retVal == true
        1 * ldapTemplate.lookup(_ as Name)
    }

    def "User does not exist"() {
        when:
        def retVal = consultantService.exists("")
        then:
        retVal == false
        1 * ldapTemplate.lookup(_ as Name) >> { throw new NameNotFoundException("test") }
    }


    def "Try update consultant that does not exist"() {
        when:
        def retVal = consultantService.updateConsultant(new Consultant(dn: "o=rfk"))

        then:
        retVal == false
        0 * ldapTemplate.update(_ as Consultant)
        1 * ldapTemplate.lookup(_ as Name) >> { throw new NameNotFoundException("test") }
    }

    def "Update existing consultant"() {
        when:
        def retVal = consultantService.updateConsultant(new Consultant(dn: "o=rfk"))

        then:
        retVal == true
        1 * ldapTemplate.update(_ as Consultant)
        1 * ldapTemplate.lookup(_ as Name) >> new Object()
    }

    def "ProgressState from Invited to Pending"() {
        when:
        def retVal = consultantService.stageConsultant(new Consultant(dn: "o=rfk", state: ConsultantState.INVITED))
        then:
        retVal == true
        1 * consultantObjectService.setupPendingConsultant(_ as Consultant)
        1 * ldapTemplate.update(_ as Consultant)
    }

    def "ProgressState from Pending to Confirmed"() {
        when:
        def retVal = consultantService.stageConsultant(new Consultant(dn: "o=rfk", state: ConsultantState.PENDING))
        then:
        retVal == true
        1 * consultantObjectService.setupConfirmedConsultant(_ as Consultant)
        1 * ldapTemplate.update(_ as Consultant)
        1 * smsNotifySerivce.notifyNewConsultantPassword(_ as Consultant)
        1 * emailNotifyService.notifyNewConsultant(_ as Consultant)
    }

    def "ProgressState for not existing consultant"() {
        when:
        def retVal = consultantService.stageConsultant(new Consultant(dn: "o=rfk"))
        then:
        retVal == false
        1 * ldapTemplate.lookup(_ as Name) >> { throw new NameNotFoundException("test") }
    }


    def "Delete consultant"() {
        when:
        def retVal = consultantService.deleteConsultant("o=rfk")
        then:
        retVal != Optional.empty()
        1 * configService.getConsultantBaseContainer() >> LdapNameBuilder.newInstance().build()
        1 * ldapTemplate.findByDn(_ as Name, _ as Class) >> new Consultant()
        1 * ldapTemplate.delete(_ as Consultant)
        1 * smsNotifySerivce.notifyDeletedConsultant(_ as Consultant)
    }

    def "Try delete consultant that does not exist"() {
        when:
        def retVal = consultantService.deleteConsultant("o=rfk")
        then:
        retVal == Optional.empty()
        1 * configService.getConsultantBaseContainer() >> LdapNameBuilder.newInstance().build()
        1 * ldapTemplate.findByDn(_ as Name, _ as Class) >> null
        0 * ldapTemplate.delete(_ as Consultant)
        0 * smsNotifySerivce.notifyDeletedConsultant(_ as Consultant)
    }

    def "GetConsultant"() {
    }

    def "GetConsultants"() {
    }
}
