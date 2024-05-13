package dbms.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "operating_system")
public class OperatingSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "os_name")
    private String osName;

    @Column(name = "os_version")
    private String osVersion;

    @OneToMany(mappedBy = "operatingSystem")
    private List<Computer> computers;
}
