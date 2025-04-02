package com.FinalProject.UMS;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ExcelDatabase {

    private static final String FILE_PATH = "src/main/resources/UMS_Data.xlsx";
    private static final String STUDENT_SHEET_NAME = "Students ";
    private static final String SUBJECT_SHEET_NAME = "Subjects ";
    private static final int ID_COLUMN = 0;
    private static final int EMAIL_COLUMN = 4;
    private static final int PASSWORD_COLUMN = 11;
    private static final int SUBJECT_CODE_COLUMN = 0;
    private static final int SUBJECT_NAME_COLUMN = 1;
    private static final boolean HAS_HEADER_ROW = true;
    private static final boolean HAS_SUBJECT_HEADER_ROW = true;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Logger LOGGER = Logger.getLogger(ExcelDatabase.class.getName());

    // Helper method to clean a string by removing non-printable characters.
    private static String cleanString(String input) {
        if (input == null) return "";
        // Remove any control (non-printable) characters.
        return input.replaceAll("\\p{C}", "");
    }

    public static Map<String, User> loadUsers() {
        Map<String, User> users = new HashMap<>();
        File excelFile = new File(FILE_PATH);
        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            DataFormatter formatter = new DataFormatter();
            Sheet sheet = workbook.getSheet(STUDENT_SHEET_NAME);
            if (sheet == null) {
                LOGGER.warning("Sheet '" + STUDENT_SHEET_NAME + "' not found in Excel file.");
                return users;
            }

            for (Row row : sheet) {
                if (HAS_HEADER_ROW && row.getRowNum() == 0) continue;

                try {
                    String id = cleanString(getCellValueAsString(row.getCell(ID_COLUMN))).trim();
                    String email = cleanString(getCellValueAsString(row.getCell(EMAIL_COLUMN))).trim();

                    // Log the password cell details for debugging
                    Cell passwordCell = row.getCell(PASSWORD_COLUMN);
                    String rawPassword = getCellValueAsString(passwordCell);
                    if (passwordCell != null) {
                        LOGGER.info("Row " + row.getRowNum() + " password cell type: " + passwordCell.getCellType());
                    }
                    LOGGER.info("Row " + row.getRowNum() + " raw password: [" + rawPassword + "]");

                    String password = cleanString(rawPassword).trim();

                    if ((id == null || id.isEmpty()) && (email == null || email.isEmpty())) {
                        LOGGER.warning("Skipping row " + row.getRowNum() + " due to missing ID and email.");
                        continue;
                    }
                    if (password == null || password.isEmpty()) {
                        LOGGER.warning("Skipping row " + row.getRowNum() + " due to missing password.");
                        continue;
                    }

                    if (email != null && !email.isEmpty() && !EMAIL_PATTERN.matcher(email).matches()) {
                        LOGGER.warning("Skipping row " + row.getRowNum() + " due to invalid email format: " + email);
                        continue;
                    }

                    Student student = loadStudentById(id);
                    User user;
                    if (student != null) {
                        LOGGER.info("Wrapping as Student: " + id);
                        user = student;
                    } else {
                        LOGGER.warning("Falling back to generic User: " + id);
                        user = new User(id, email, password);
                    }

                    if (id != null && !id.isEmpty()) users.put(id, user);
                    if (email != null && !email.isEmpty()) users.put(email, user);

                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error processing row " + row.getRowNum() + ": " + e.getMessage(), e);
                }
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading Excel file: " + e.getMessage(), e);
        }
        return users;
    }

    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        File excelFile = new File(FILE_PATH);
        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(STUDENT_SHEET_NAME);
            if (sheet == null) {
                LOGGER.warning("Sheet '" + STUDENT_SHEET_NAME + "' not found in Excel file.");
                return students;
            }

            Iterator<Row> rowIterator = sheet.rowIterator();
            if (HAS_HEADER_ROW && rowIterator.hasNext()) rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                try {
                    String studentId = cleanString(getCellValueAsString(row.getCell(ID_COLUMN))).trim();
                    String name = cleanString(getCellValueAsString(row.getCell(1)));
                    String address = cleanString(getCellValueAsString(row.getCell(2)));
                    String telephone = cleanString(getCellValueAsString(row.getCell(3)));
                    String email = cleanString(getCellValueAsString(row.getCell(4)));
                    String academicLevel = cleanString(getCellValueAsString(row.getCell(5)));
                    String currentSemester = cleanString(getCellValueAsString(row.getCell(6)));
                    String profilePhoto = cleanString(getCellValueAsString(row.getCell(7)));
                    String subjectsRegistered = cleanString(getCellValueAsString(row.getCell(8)));
                    String thesisTitle = cleanString(getCellValueAsString(row.getCell(9)));
                    String progressStr = cleanString(getCellValueAsString(row.getCell(10)));
                    double progress = 0.0;
                    if (progressStr != null && !progressStr.isEmpty()) {
                        progressStr = progressStr.replaceAll("[^\\d.%]", "").replace("%", "");
                        progress = Double.parseDouble(progressStr) / 100.0;
                    }
                    String password = cleanString(getCellValueAsString(row.getCell(PASSWORD_COLUMN))).trim();

                    String[] names = name.split(" ", 2);
                    String firstName = names.length > 0 ? names[0] : "";
                    String lastName = names.length > 1 ? names[1] : "";

                    students.add(new Student(studentId, firstName, lastName, address, telephone, email,
                            academicLevel, currentSemester, profilePhoto, subjectsRegistered,
                            thesisTitle, progress, password));

                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error processing row: " + row.getRowNum() + " - " + e.getMessage(), e);
                }
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading Excel file: " + e.getMessage(), e);
        }
        return students;
    }

    public static Map<String, String> loadSubjects() {
        Map<String, String> subjects = new HashMap<>();
        File excelFile = new File(FILE_PATH);

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet subjectSheet = workbook.getSheet(SUBJECT_SHEET_NAME);
            if (subjectSheet == null) {
                LOGGER.warning("Sheet '" + SUBJECT_SHEET_NAME + "' not found in Excel file.");
                return subjects;
            }

            for (Row row : subjectSheet) {
                if (HAS_SUBJECT_HEADER_ROW && row.getRowNum() == 0) continue;

                try {
                    String subjectCode = cleanString(getCellValueAsString(row.getCell(SUBJECT_CODE_COLUMN)));
                    String subjectName = cleanString(getCellValueAsString(row.getCell(SUBJECT_NAME_COLUMN)));

                    if (subjectCode == null || subjectCode.isEmpty()) continue;
                    if (subjectName == null || subjectName.isEmpty()) continue;

                    subjects.put(subjectCode, subjectName);

                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error processing subject row " + row.getRowNum() + ": " + e.getMessage(), e);
                }
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading Excel file for subjects: " + e.getMessage(), e);
        }
        return subjects;
    }

    public static void addSubjectToExcel(Subject subject, String filePath, String sheetName) {
        File excelFile = new File(filePath);
        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(SUBJECT_CODE_COLUMN).setCellValue("Subject Code");
                headerRow.createCell(SUBJECT_NAME_COLUMN).setCellValue("Subject Name");
            }

            int nextRow = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(nextRow);
            row.createCell(SUBJECT_CODE_COLUMN).setCellValue(subject.getCode());
            row.createCell(SUBJECT_NAME_COLUMN).setCellValue(subject.getName());

            try (FileOutputStream fos = new FileOutputStream(excelFile)) {
                workbook.write(fos);
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error adding subject to Excel: " + e.getMessage(), e);
        }
    }

    public static void deleteSubjectFromExcel(Subject subject, String filePath, String sheetName) {
        File excelFile = new File(filePath);
        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                LOGGER.warning("Sheet " + sheetName + " not found in Excel file.");
                return;
            }

            int rowToDelete = -1;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    String code = cleanString(getCellValueAsString(row.getCell(SUBJECT_CODE_COLUMN)));
                    String name = cleanString(getCellValueAsString(row.getCell(SUBJECT_NAME_COLUMN)));
                    if (subject.getCode().equals(code) && subject.getName().equals(name)) {
                        rowToDelete = i;
                        break;
                    }
                }
            }

            if (rowToDelete != -1) {
                sheet.removeRow(sheet.getRow(rowToDelete));
                int lastRowNum = sheet.getLastRowNum();
                if (rowToDelete < lastRowNum) {
                    sheet.shiftRows(rowToDelete + 1, lastRowNum, -1);
                }
                try (FileOutputStream fos = new FileOutputStream(excelFile)) {
                    workbook.write(fos);
                }
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error deleting subject from Excel: " + e.getMessage(), e);
        }
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        try {
            DataFormatter formatter = new DataFormatter();
            return formatter.formatCellValue(cell).trim();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error getting cell value: " + e.getMessage(), e);
            return "";
        }
    }

    public static Student loadStudentById(String studentId) {
        File excelFile = new File(FILE_PATH);
        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(STUDENT_SHEET_NAME);
            if (sheet == null) {
                LOGGER.warning("Sheet '" + STUDENT_SHEET_NAME + "' not found in Excel file.");
                return null;
            }

            Iterator<Row> rowIterator = sheet.rowIterator();
            if (HAS_HEADER_ROW && rowIterator.hasNext()) rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                try {
                    String currentStudentId = cleanString(getCellValueAsString(row.getCell(ID_COLUMN))).trim();
                    if (studentId.equals(currentStudentId)) {
                        String name = cleanString(getCellValueAsString(row.getCell(1)));
                        String address = cleanString(getCellValueAsString(row.getCell(2)));
                        String telephone = cleanString(getCellValueAsString(row.getCell(3)));
                        String email = cleanString(getCellValueAsString(row.getCell(4)));
                        String academicLevel = cleanString(getCellValueAsString(row.getCell(5)));
                        String currentSemester = cleanString(getCellValueAsString(row.getCell(6)));
                        String profilePhoto = cleanString(getCellValueAsString(row.getCell(7)));
                        String subjectsRegistered = cleanString(getCellValueAsString(row.getCell(8)));
                        String thesisTitle = cleanString(getCellValueAsString(row.getCell(9)));
                        String progressStr = cleanString(getCellValueAsString(row.getCell(10)));
                        double progress = 0.0;
                        if (progressStr != null && !progressStr.isEmpty()) {
                            progressStr = progressStr.replaceAll("[^\\d.%]", "").replace("%", "");
                            progress = Double.parseDouble(progressStr) / 100.0;
                        }
                        String password = cleanString(getCellValueAsString(row.getCell(PASSWORD_COLUMN))).trim();

                        String[] names = name.split(" ", 2);
                        String firstName = names.length > 0 ? names[0] : "";
                        String lastName = names.length > 1 ? names[1] : "";

                        return new Student(studentId, firstName, lastName, address, telephone, email,
                                academicLevel, currentSemester, profilePhoto, subjectsRegistered,
                                thesisTitle, progress, password);
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error processing row: " + row.getRowNum() + " - " + e.getMessage(), e);
                }
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading Excel file: " + e.getMessage(), e);
        }
        return null;
    }
}
