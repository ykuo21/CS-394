import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

public class JavaFXTemplate extends Application{

    // Define form fields
    private ComboBox<String> roleComboBox;
    private TextField nameField;
    private TextField idField;
    private TextField emailField;
    private TextField phoneField;
    private TextField addressField;
    private ComboBox<String> departmentComboBox;

    // Role-specific fields
    private VBox studentFields = new VBox(10);
    private VBox facultyFields = new VBox(10);
    private VBox staffFields = new VBox(10);

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("University Recertification Form");

        // Create main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));

        // Create form header
        VBox headerBox = createHeader();
        mainLayout.setTop(headerBox);

        // Create form content
        VBox formContent = createFormContent();
        mainLayout.setCenter(formContent);

        // Create buttons
        HBox buttonBar = createButtonBar();
        mainLayout.setBottom(buttonBar);

        Scene scene = new Scene(mainLayout, 700, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("University Recertification Form");
        titleLabel.getStyleClass().add("title-label");

        Label subtitleLabel = new Label("Please complete all required fields for annual recertification");
        subtitleLabel.getStyleClass().add("subtitle-label");

        header.getChildren().addAll(titleLabel, subtitleLabel);
        header.setPadding(new Insets(0, 0, 20, 0));

        return header;
    }

    private VBox createFormContent() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(10));

        // Create common information section
        TitledPane commonInfoSection = createCommonInfoSection();

        // Create role-specific sections
        TitledPane roleSpecificSection = createRoleSpecificSection();

        // Create emergency contact section
        TitledPane emergencyContactSection = createEmergencyContactSection();

        // Create declarations section
        TitledPane declarationsSection = createDeclarationsSection();

        content.getChildren().addAll(commonInfoSection, roleSpecificSection,
                emergencyContactSection, declarationsSection);

        return content;
    }

    private TitledPane createCommonInfoSection() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        // Role selection
        Label roleLabel = new Label("Your Role at the University*");
        String[] roles = {"Student", "Faculty/Professor", "Staff", "Administrator", "Other"};
        roleComboBox = new ComboBox<>(FXCollections.observableArrayList(roles));
        roleComboBox.setPromptText("Select your role");
        roleComboBox.setPrefWidth(300);

        // Role change listener
        roleComboBox.setOnAction(e -> updateRoleSpecificFields());

        // Basic personal information
        Label nameLabel = new Label("Full Legal Name*");
        nameField = new TextField();
        nameField.setPromptText("Enter your full legal name");

        Label idLabel = new Label("University ID Number*");
        idField = new TextField();
        idField.setPromptText("Enter your university ID");

        Label contactInfoLabel = new Label("Contact Information");
        contactInfoLabel.getStyleClass().add("section-label");

        Label emailLabel = new Label("Email Address*");
        emailField = new TextField();
        emailField.setPromptText("Enter your email address");

        Label phoneLabel = new Label("Phone Number*");
        phoneField = new TextField();
        phoneField.setPromptText("Enter your phone number");

        Label addressLabel = new Label("Current Address*");
        addressField = new TextField();
        addressField.setPromptText("Enter your current address");

        Label departmentLabel = new Label("Department/Faculty*");
        String[] departments = {"Arts and Humanities", "Business", "Education", "Engineering",
                "Law", "Medicine", "Sciences", "Social Sciences", "Other"};
        departmentComboBox = new ComboBox<>(FXCollections.observableArrayList(departments));
        departmentComboBox.setPromptText("Select your department");
        departmentComboBox.setPrefWidth(300);

        content.getChildren().addAll(
                roleLabel, roleComboBox,
                nameLabel, nameField,
                idLabel, idField,
                contactInfoLabel,
                emailLabel, emailField,
                phoneLabel, phoneField,
                addressLabel, addressField,
                departmentLabel, departmentComboBox
        );

        TitledPane titledPane = new TitledPane("Personal Information", content);
        titledPane.setExpanded(true);

        return titledPane;
    }

    private TitledPane createRoleSpecificSection() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        // Initialize role-specific fields
        initStudentFields();
        initFacultyFields();
        initStaffFields();

        Label sectionInfo = new Label("Please complete the fields specific to your role");
        sectionInfo.getStyleClass().add("section-info");

        content.getChildren().add(sectionInfo);
        content.getChildren().add(studentFields);
        studentFields.setVisible(false);
        content.getChildren().add(facultyFields);
        facultyFields.setVisible(false);
        content.getChildren().add(staffFields);
        staffFields.setVisible(false);

        TitledPane titledPane = new TitledPane("Role-Specific Information", content);
        titledPane.setExpanded(true);

        return titledPane;
    }

    private void initStudentFields() {
        studentFields.setPadding(new Insets(10));

        Label programLabel = new Label("Academic Program/Major*");
        ComboBox<String> programComboBox = new ComboBox<>(FXCollections.observableArrayList(
                "Bachelor of Arts", "Bachelor of Science", "Master of Arts",
                "Master of Science", "PhD", "Other"
        ));
        programComboBox.setPromptText("Select your program");
        programComboBox.setPrefWidth(300);

        Label enrollmentLabel = new Label("Enrollment Status*");
        ToggleGroup enrollmentGroup = new ToggleGroup();
        RadioButton fullTimeRadio = new RadioButton("Full-time");
        fullTimeRadio.setToggleGroup(enrollmentGroup);
        RadioButton partTimeRadio = new RadioButton("Part-time");
        partTimeRadio.setToggleGroup(enrollmentGroup);
        HBox enrollmentBox = new HBox(20, fullTimeRadio, partTimeRadio);

        Label gradDateLabel = new Label("Expected Graduation Date*");
        DatePicker gradDatePicker = new DatePicker();

        Label financialAidLabel = new Label("Financial Aid Status");
        ComboBox<String> financialAidComboBox = new ComboBox<>(FXCollections.observableArrayList(
                "Receiving Financial Aid", "Not Receiving Financial Aid", "Pending"
        ));
        financialAidComboBox.setPromptText("Select your status");
        financialAidComboBox.setPrefWidth(300);

        Label insuranceLabel = new Label("Health Insurance Verification*");
        CheckBox insuranceCheck = new CheckBox("I confirm my health insurance information is up to date");

        Label vaccinationLabel = new Label("Vaccination Records*");
        CheckBox vaccinationCheck = new CheckBox("I confirm my vaccination records are up to date");

        studentFields.getChildren().addAll(
                programLabel, programComboBox,
                enrollmentLabel, enrollmentBox,
                gradDateLabel, gradDatePicker,
                financialAidLabel, financialAidComboBox,
                insuranceLabel, insuranceCheck,
                vaccinationLabel, vaccinationCheck
        );
    }

    private void initFacultyFields() {
        facultyFields.setPadding(new Insets(10));

        Label positionLabel = new Label("Position/Title*");
        ComboBox<String> positionComboBox = new ComboBox<>(FXCollections.observableArrayList(
                "Professor", "Associate Professor", "Assistant Professor",
                "Lecturer", "Instructor", "Research Fellow", "Other"
        ));
        positionComboBox.setPromptText("Select your position");
        positionComboBox.setPrefWidth(300);

        Label officeLabel = new Label("Office Location*");
        TextField officeField = new TextField();
        officeField.setPromptText("Enter your office location");

        Label hoursLabel = new Label("Office Hours");
        TextField hoursField = new TextField();
        hoursField.setPromptText("Enter your office hours");

        Label researchLabel = new Label("Current Research Projects");
        TextArea researchArea = new TextArea();
        researchArea.setPromptText("List your current research projects");
        researchArea.setPrefRowCount(3);

        Label teachingLabel = new Label("Teaching Assignments for Upcoming Term*");
        TextArea teachingArea = new TextArea();
        teachingArea.setPromptText("List your upcoming teaching assignments");
        teachingArea.setPrefRowCount(3);

        Label devLabel = new Label("Professional Development Activities (Past Year)");
        TextArea devArea = new TextArea();
        devArea.setPromptText("List your professional development activities");
        devArea.setPrefRowCount(3);

        facultyFields.getChildren().addAll(
                positionLabel, positionComboBox,
                officeLabel, officeField,
                hoursLabel, hoursField,
                researchLabel, researchArea,
                teachingLabel, teachingArea,
                devLabel, devArea
        );
    }

    private void initStaffFields() {
        staffFields.setPadding(new Insets(10));

        Label positionLabel = new Label("Position/Title*");
        TextField positionField = new TextField();
        positionField.setPromptText("Enter your position/title");

        Label supervisorLabel = new Label("Supervisor Name*");
        TextField supervisorField = new TextField();
        supervisorField.setPromptText("Enter your supervisor's name");

        Label certLabel = new Label("Required Certifications");
        TextArea certArea = new TextArea();
        certArea.setPromptText("List any certifications that require renewal");
        certArea.setPrefRowCount(3);

        Label devLabel = new Label("Professional Development Completed (Past Year)");
        TextArea devArea = new TextArea();
        devArea.setPromptText("List your professional development activities");
        devArea.setPrefRowCount(3);

        staffFields.getChildren().addAll(
                positionLabel, positionField,
                supervisorLabel, supervisorField,
                certLabel, certArea,
                devLabel, devArea
        );
    }

    private void updateRoleSpecificFields() {
        String selectedRole = roleComboBox.getValue();

        studentFields.setVisible("Student".equals(selectedRole));
        facultyFields.setVisible("Faculty/Professor".equals(selectedRole));
        staffFields.setVisible("Staff".equals(selectedRole) || "Administrator".equals(selectedRole));
    }

    private TitledPane createEmergencyContactSection() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        Label nameLabel = new Label("Emergency Contact Name*");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter emergency contact name");

        Label relationshipLabel = new Label("Relationship to You*");
        TextField relationshipField = new TextField();
        relationshipField.setPromptText("Enter relationship");

        Label phoneLabel = new Label("Emergency Contact Phone*");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Enter emergency contact phone");

        Label emailLabel = new Label("Emergency Contact Email");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter emergency contact email");

        content.getChildren().addAll(
                nameLabel, nameField,
                relationshipLabel, relationshipField,
                phoneLabel, phoneField,
                emailLabel, emailField
        );

        TitledPane titledPane = new TitledPane("Emergency Contact Information", content);
        titledPane.setExpanded(true);

        return titledPane;
    }

    private TitledPane createDeclarationsSection() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        Label complianceLabel = new Label("Compliance Trainings");
        CheckBox complianceCheck = new CheckBox(
                "I confirm that I have completed all required compliance trainings for my role"
        );

        Label conflictLabel = new Label("Conflict of Interest");
        CheckBox conflictCheck = new CheckBox(
                "I confirm that I have disclosed all potential conflicts of interest"
        );

        Label policyLabel = new Label("University Policies");
        CheckBox policyCheck = new CheckBox(
                "I understand and agree to comply with all university policies and procedures"
        );

        Label accuracyLabel = new Label("Information Accuracy");
        CheckBox accuracyCheck = new CheckBox(
                "I confirm that all information provided is accurate and complete"
        );

        content.getChildren().addAll(
                complianceLabel, complianceCheck,
                conflictLabel, conflictCheck,
                policyLabel, policyCheck,
                accuracyLabel, accuracyCheck
        );

        TitledPane titledPane = new TitledPane("Declarations and Agreements", content);
        titledPane.setExpanded(true);

        return titledPane;
    }

    private HBox createButtonBar() {
        HBox buttonBar = new HBox(15);
        buttonBar.setPadding(new Insets(20, 0, 0, 0));
        buttonBar.setAlignment(Pos.CENTER_RIGHT);

        Button saveButton = new Button("Save Progress");
        saveButton.getStyleClass().add("secondary-button");
        saveButton.setOnAction(e -> handleSave());

        Button submitButton = new Button("Submit Form");
        submitButton.getStyleClass().add("primary-button");
        submitButton.setOnAction(e -> handleSubmit());

        buttonBar.getChildren().addAll(saveButton, submitButton);

        return buttonBar;
    }

    private void handleSave() {
        showAlert(Alert.AlertType.INFORMATION, "Save Progress",
                "Your form progress has been saved successfully.");
    }

    private void handleSubmit() {
        // Validation would go here
        if (validateForm()) {
            showAlert(Alert.AlertType.INFORMATION, "Form Submitted",
                    "Your recertification form has been submitted successfully.");
        }
    }

    private boolean validateForm() {
        // Simple validation - check if required fields are filled
        if (roleComboBox.getValue() == null ||
                nameField.getText().trim().isEmpty() ||
                idField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                phoneField.getText().trim().isEmpty() ||
                addressField.getText().trim().isEmpty() ||
                departmentComboBox.getValue() == null) {

            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Please fill in all required fields marked with an asterisk (*).");
            return false;
        }

        // Role-specific validation would go here

        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}