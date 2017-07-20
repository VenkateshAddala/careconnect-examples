package uk.nhs.careconnect.examples.App;


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.AuditEvent;
import ca.uhn.fhir.parser.IParser;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.nhs.careconnect.examples.fhir.CareConnectAuditEvent;

import javax.jms.*;

@SpringBootApplication
public class IGExplore implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(IGExplore.class, args);
	}

    IParser XMLparser = null;

    IParser JSONparser = null;
    @Override
	public void run(String... args) throws Exception {

        if (args.length > 0 && args[0].equals("exitcode")) {
            throw new Exception();
        }
        FhirContext ctxFHIR = FhirContext.forDstu2();
        XMLparser = ctxFHIR.newXmlParser();

        JSONparser = ctxFHIR.newJsonParser();


        AuditEvent audit = CareConnectAuditEvent.buildAuditEvent("rest");
        System.out.println(XMLparser.setPrettyPrint(true).encodeResourceToString(audit));
        sendToAudit(audit);
    }
		/*

		// This is to base HAPI server not the CareConnectAPI
		String serverBase = "http://127.0.0.1:8080/FHIRServer/DSTU2/";
        // String serverBase = "http://fhirtest.uhn.ca/baseDstu2/";

		IGenericClient client = ctxFHIR.newRestfulGenericClient(serverBase);

        Organization organisation = CareConnectOrganisation.buildCareConnectOrganisation(
                "RTG",
                "Derby Teaching Hospitals NHS Foundation Trust",
                "01332 340131",
                "Uttoxeter Road",
                "",
                    "Derby",
                    "DE22 3NE"
                    , "prov"
                );
        System.out.println(parser.setPrettyPrint(true).encodeResourceToString(organisation));
        MethodOutcome outcome = client.update().resource(organisation)
                .conditionalByUrl("Organization?identifier="+organisation.getIdentifier().get(0).getSystem()+"%7C"+organisation.getIdentifier().get(0).getValue())
                .execute();
        organisation.setId(outcome.getId());
        System.out.println(outcome.getId().getValue());

        // GP Practice
        Organization practice = CareConnectOrganisation.buildCareConnectOrganisation(
                "C81010",
                "The Moir Medical Centre",
                "0115 9737320",
                "Regent Street",
                "Long Eaton",
                "Nottingham",
                "NG10 1QQ"
                , "prov"
        );
        System.out.println(parser.setPrettyPrint(true).encodeResourceToString(practice));
        outcome = client.update().resource(practice)
                .conditionalByUrl("Organization?identifier="+practice.getIdentifier().get(0).getSystem()+"%7C"+practice.getIdentifier().get(0).getValue())
                .execute();
        practice.setId(outcome.getId());
        System.out.println(outcome.getId().getValue());


        Practitioner gp = CareConnectPractitioner.buildCareConnectPractitioner(
                "G8133438",
                "Bhatia",
                "AA",
                "Dr.",
                AdministrativeGenderEnum.MALE,
                "0115 9737320",
                "Regent Street",
                "Long Eaton",
                "Nottingham",
                "NG10 1QQ",
                practice,
                "R0260",
                "General Medical Practitioner"
        );
        System.out.println(parser.setPrettyPrint(true).encodeResourceToString(gp));
        outcome = client.update().resource(gp)
                .conditionalByUrl("Practitioner?identifier="+gp.getIdentifier().get(0).getSystem()+"%7C"+gp.getIdentifier().get(0).getValue())
                .execute();
        gp.setId(outcome.getId());
        System.out.println(outcome.getId().getValue());

        Practitioner gp2 = CareConnectPractitioner.buildCareConnectPractitioner(
                "G8650149",
                "Swamp",
                "Karren",
                "Dr.",
                AdministrativeGenderEnum.MALE,
                "0115 9737320",
                "Regent Street",
                "Long Eaton",
                "Nottingham",
                "NG10 1QQ",
                practice,
                "R0260",
                "General Medical Practitioner"
        );
        System.out.println(parser.setPrettyPrint(true).encodeResourceToString(gp2));
        outcome = client.update().resource(gp2)
                .conditionalByUrl("Practitioner?identifier="+gp2.getIdentifier().get(0).getSystem()+"%7C"+gp2.getIdentifier().get(0).getValue())
                .execute();
        gp2.setId(outcome.getId());
        System.out.println(outcome.getId().getValue());


        Patient patient = CareConnectPatient.buildCareConnectPatientCSV("British - Mixed British,01,9876543210,Number present and verified,01,Kanfeld,Bernie,Miss,10 Field Jardin,Long Eaton,Nottingham,NG10 1ZZ,1,1998-03-19"
                ,practice
                ,gp );

		System.out.println(parser.setPrettyPrint(true).encodeResourceToString(patient));
		outcome = client.update().resource(patient)
                            .conditionalByUrl("Patient?identifier="+patient.getIdentifier().get(0).getSystem()+"%7C"+patient.getIdentifier().get(0).getValue())
                            .execute();
        patient.setId(outcome.getId());
        System.out.println(outcome.getId().getValue());


        MedicationOrder prescription = CareConnectMedicationOrder.buildCareConnectMedicationOrder(patient,gp);
        System.out.println(parser.setPrettyPrint(true).encodeResourceToString(prescription));
        outcome = client.update().resource(prescription)
                .conditionalByUrl("MedicationOrder?identifier="+prescription.getIdentifier().get(0).getSystem()+"%7C"+prescription.getIdentifier().get(0).getValue())
                .execute();
        prescription.setId(outcome.getId());
        System.out.println(outcome.getId().getValue());

        MedicationStatement medicationSummary = CareConnectMedicationStatement.buildCareConnectMedicationStatement(patient,gp2);
        System.out.println(parser.setPrettyPrint(true).encodeResourceToString(medicationSummary));
        outcome = client.update().resource(medicationSummary)
                .conditionalByUrl("MedicationStatement?identifier="+medicationSummary.getIdentifier().get(0).getSystem()+"%7C"+medicationSummary.getIdentifier().get(0).getValue())
                .execute();
        medicationSummary.setId(outcome.getId());
        System.out.println(outcome.getId().getValue());


        Immunization
                immunisation = CareConnectImmunization.buildCareConnectImmunization(patient, gp);
        System.out.println(parser.setPrettyPrint(true).encodeResourceToString(immunisation));
        outcome = client.update().resource(immunisation)
                .conditionalByUrl("Immunization?identifier="+immunisation.getIdentifier().get(0).getSystem()+"%7C"+immunisation.getIdentifier().get(0).getValue())
                .execute();
        immunisation.setId(outcome.getId());
        System.out.println(outcome.getId().getValue());

     */

    private void sendToAudit(AuditEvent audit) {
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("Elastic.Queue");

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Create a messages

            String text =JSONparser.setPrettyPrint(true).encodeResourceToString(audit);
            TextMessage message = session.createTextMessage(text);

            // Tell the producer to send the message
            System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
            producer.send(message);

            // Clean up
            session.close();
            connection.close();
        }
        catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }
}


