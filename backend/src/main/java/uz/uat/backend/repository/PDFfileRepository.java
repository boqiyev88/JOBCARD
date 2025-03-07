package uz.uat.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.PdfFile;

@Repository
public interface PDFfileRepository extends JpaRepository<PdfFile, String> {

}
