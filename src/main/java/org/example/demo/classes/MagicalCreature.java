package org.example.demo.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MagicalCreature implements Serializable, Comparable<MagicalCreature> {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        private String name;
        @Enumerated(EnumType.STRING)
        private Alignment alignment;
        private String items;
        private LocalDate dateFound;
        private String founderName;
        private String founderSurname;
        private boolean isItEndangered;

        public MagicalCreature(String name, Alignment alignment, String items, LocalDate dateFound, String founderName, String founderSurname, boolean isItEndangered) {
                this.name = name;
                this.alignment = alignment;
                this.items = items;
                this.dateFound = dateFound;
                this.founderName = founderName;
                this.founderSurname = founderSurname;
                this.isItEndangered = isItEndangered;
        }

        @Override
        public String toString() {
                return name + " " + founderName + " " + founderSurname + " " + dateFound;
        }

        @Override
        public int compareTo(MagicalCreature o) {
                if(this.name.compareTo(o.name) > 0){
                        return 1;
                }
                else if(this.dateFound.compareTo(o.dateFound) > 0){
                        return -1;
                }
                else{
                        return 0;
                }
        }
}
