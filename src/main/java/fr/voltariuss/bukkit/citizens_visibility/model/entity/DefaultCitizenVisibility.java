package fr.voltariuss.bukkit.citizens_visibility.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cv_default_citizen_visibility")
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class DefaultCitizenVisibility {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cv_id", nullable = false, updatable = false)
  private long id;

  @Column(name = "cv_citizen_id")
  @Setter
  private int citizenId;

  @Column(name = "cv_is_visible_by_default")
  @Setter
  private boolean isVisibleByDefault;
}
