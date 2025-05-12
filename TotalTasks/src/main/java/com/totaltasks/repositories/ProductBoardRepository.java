package com.totaltasks.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.totaltasks.entities.ProductBoardEntity;

@Repository
public interface ProductBoardRepository extends JpaRepository<ProductBoardEntity, Long> {
    List<ProductBoardEntity> findByProyecto_idProyecto(Long idProyecto);
}