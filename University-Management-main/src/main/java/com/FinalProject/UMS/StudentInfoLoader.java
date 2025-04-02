package com.FinalProject.UMS;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentInfoLoader {

    public List<Student> loadStudentInfo(List<Subject> subjectsFromDatabase) throws IOException {
        List<Student> students = new ArrayList<>();

        InputStream inputStream = getClass().getResourceAsStream("/UMS_Data.xlsx");
        if (inputStream == null) {
            throw new IOException("UMS_Data.xlsx not found in resources");
        }

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheet("Students");
            if (sheet == null) {
                throw new IOException("Students sheet not found in UMS_Data.xlsx");
            }

            int lastRow = sheet.getLastRowNum();

            for (int i = 1; i <= lastRow; i++) {  // skip header row
                Row row = sheet.getRow(i);
                if (row == null) break; // stop at empty rows

                String studentId = getCellValueAsString(row.getCell(0));
                if (studentId.isBlank()) break;

                // Read full name and split into first and last names.
                String fullName = getCellValueAsString(row.getCell(1));
                String firstName = "";
                String lastName = "";
                if (!fullName.isBlank()) {
                    String[] nameParts = fullName.split(" ", 2);
                    firstName = nameParts[0];
                    if (nameParts.length > 1) {
                        lastName = nameParts[1];
                    }
                }

                // Note: Adjust the order if your Excel columns differ.
                String address = getCellValueAsString(row.getCell(2));
                String phoneNumber = getCellValueAsString(row.getCell(3));
                String email = getCellValueAsString(row.getCell(4));
                String academicLevel = getCellValueAsString(row.getCell(5));
                String currentSemester = getCellValueAsString(row.getCell(6));
                String profilePhoto = getCellValueAsString(row.getCell(7));
                String subjectsStr = getCellValueAsString(row.getCell(8));
                String thesisTitle = getCellValueAsString(row.getCell(9));

                double progress = 0.0;
                Cell progressCell = row.getCell(10);
                if (progressCell != null && progressCell.getCellType() == CellType.NUMERIC) {
                    progress = progressCell.getNumericCellValue();
                }

                // Read password from column 11.
                String password = getCellValueAsString(row.getCell(11));

                List<Subject> subjectsRegistered = new ArrayList<>();
                if (!subjectsStr.isBlank()) {
                    for (String subjectCode : subjectsStr.split(",")) {
                        Subject subject = findSubjectByCode(subjectCode.trim(), subjectsFromDatabase);
                        if (subject != null) {
                            subjectsRegistered.add(subject);
                        } else {
                            System.out.println("Subject not found: " + subjectCode.trim());
                        }
                    }
                }

                // Create the Student object with 13 parameters:
                // studentId, firstName, lastName, address, telephone, email, academicLevel,
                // currentSemester, profilePhoto, subjectsRegistered, thesisTitle, progress, password
                Student student = new Student(
                        studentId,
                        firstName,
                        lastName,
                        address,
                        phoneNumber,
                        email,
                        academicLevel,
                        currentSemester,
                        profilePhoto,
                        subjectsRegistered.toString(),
                        thesisTitle,
                        progress,
                        password
                );

                students.add(student);
            }
        }

        return students;
    }

    private Subject findSubjectByCode(String code, List<Subject> subjects) {
        for (Subject subject : subjects) {
            if (subject.getCode().equalsIgnoreCase(code)) {
                return subject;
            }
        }
        return null;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                // Remove any trailing .0 if not needed
                double d = cell.getNumericCellValue();
                if (d == Math.floor(d)) {
                    return String.valueOf((long) d);
                }
                return String.valueOf(d);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getStringCellValue().trim();
            default:
                return "";
        }
    }
}
