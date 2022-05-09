package fr.voltariuss.bukkit.citizens_visibility.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "cv_default_citizen_visibility")
@ToString
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DefaultCitizenVisibility {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cv_id", nullable = false, updatable = false)
  private long id;

  @NaturalId
  @Column(name = "cv_citizen_id")
  @Setter
  private int citizenId;

  @Column(name = "cv_is_visible_by_default")
  @Setter
  private boolean isVisibleByDefault;
}
