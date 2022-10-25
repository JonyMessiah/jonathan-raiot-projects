package com.raiot.raiotprojects.dao;

import com.raiot.raiotprojects.interfaces.DAOInterface;
import com.raiot.raiotprojects.models.ProjectModel;
import com.raiot.raiotprojects.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class ProjectDao implements DAOInterface<ProjectModel>  {
    private List<ProjectModel> projects = new ArrayList<>();

    @Override
    public void save(ProjectModel project) {
        projects.add(project);
    }
}


