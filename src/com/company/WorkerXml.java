package com.company;

import com.company.models.Task;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * is class work with xml save, update, add, delete
 */
public class WorkerXml {

    public static final String UPDATE_TIME = "update_time";
    public static final String TASK = "task";
    public static final String URL = "url";
    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String ATTEMPTS = "attempts";
    public static final String ATTEMPTS_OK = "attempts_ok";
    public static final String DASHBOARD = "dashboard";
    public static final String TIME = "time";
    public static final String THREADS = "threads";
    public static final String COUNT = "COUNT";
    private volatile Document doc;

    public WorkerXml() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            dbf.setValidating(false);
            db = dbf.newDocumentBuilder();
            File xml = new File(Config.NAME_XML);
            doc = db.parse(xml);
            doc.setXmlStandalone(true);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get time update from xml if error return default value
     * @return Integer time update
     */
    public Integer getTimeUpdate() {
        if (doc == null) {
            return null;
        }
        NodeList nodeList = doc.getElementsByTagName(UPDATE_TIME);

        if (nodeList.getLength() == 0) {
            return Config.DEFAULT_TIME_UPDATE;
        } else {
            Node time = nodeList.item(0);
            if (time != null) {
                try {
                    return Integer.valueOf(time.getAttributes().getNamedItem(TIME).getTextContent());
                } catch (NumberFormatException e) {
                    return Config.DEFAULT_TIME_UPDATE;
                }
            } else {
                return Config.DEFAULT_TIME_UPDATE;
            }
        }
    }

    public Task getById(Integer id) {
        if (id == null) {
            return null;
        } else {
            return parseTask(getNodeById(id));
        }
    }

    /**
     * get node by id
     * @param id Integer
     * @return Node node
     */
    private Node getNodeById(Integer id) {
        if (id == null) {
            return null;
        }
        NodeList nodeList = doc.getElementsByTagName(TASK);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getAttributes().getNamedItem(ID).getTextContent().equals(id.toString())) {
                return node;
            }
        }
        return null;
    }

    /**
     * parse Node from Task
     * @param task Task
     * @return Element node
     */
    private Element parseToNode(Task task) {
        if (task == null) {
            return null;
        }
        Element nodeTask = doc.createElement(TASK);
        if (task.getId() != null) {
            nodeTask.setAttribute(ID, String.valueOf(task.getId()));
        }
        if (task.getUrl() != null) {
            nodeTask.setAttribute(URL, task.getUrl());
        }
        if (task.getName() != null) {
            nodeTask.setAttribute(NAME, task.getName());
        }
        if (task.getAttempts() != null) {
            nodeTask.setAttribute(ATTEMPTS, String.valueOf(task.getAttempts()));
        }
        if (task.getAttemptsOk() != null) {
            nodeTask.setAttribute(ATTEMPTS_OK, String.valueOf(task.getAttemptsOk()));
        }
        if (task.getTime() != null) {
            nodeTask.setAttribute(TIME, String.valueOf(task.getTime()));
        }
        return nodeTask;
    }

    /**
     * add task into xml
     * @param task Task
     * @return booolean is added task
     */
    public boolean addTask(Task task) {
        Element nodeTask = parseToNode(task);
        Node dashboard = getDashboard();
        NodeList nodeList = doc.getElementsByTagName(TASK);
        Node node = nodeList.item(nodeList.getLength() - 1);
        Integer lastId = Integer.valueOf(node.getAttributes().getNamedItem(ID).getTextContent());
        lastId++;
        nodeTask.setAttribute(ID, String.valueOf(lastId));
        dashboard.appendChild(nodeTask);
        try {
            flush();
            return true;
        } catch (TransformerException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * write into xml
     * @throws TransformerException
     */
    private void flush() throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory
                .newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(Config.NAME_XML));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(source, result);
    }

    /**
     * get all tasks
     * @return List<Task>
     */
    public List<Task> getTasks() {
        NodeList nodeList = doc.getElementsByTagName(TASK);
        List<Task> taskList = new ArrayList<Task>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Task task = parseTask(nodeList.item(i));
            taskList.add(task);
        }
        return taskList;
    }

    /**
     * parse task from node
     * @param node Node
     * @return Task
     */
    private Task parseTask(Node node) {
        if (node == null) {
            return null;
        }
        boolean returnNull = true;
        NamedNodeMap map = node.getAttributes();
        Task task = new Task();
        Node id = map.getNamedItem(ID);
        if (id != null) {
            if (id.getTextContent() != null) {
                returnNull = false;
                task.setId(Integer.parseInt(id.getTextContent()));
            }
        }
        Node url = map.getNamedItem(URL);
        if (url != null) {
            if (url.getTextContent() != null) {
                returnNull = false;
                task.setUrl(url.getTextContent());
            }
        }
        Node name = map.getNamedItem(NAME);
        if (name != null) {
            if (name.getTextContent() != null) {
                returnNull = false;
                task.setName(name.getTextContent());
            }
        }
        Node attempts = map.getNamedItem(ATTEMPTS);
        if (attempts != null && attempts.getTextContent() != null) {
            returnNull = false;
            task.setAttempts(Long.parseLong(attempts.getTextContent()));
        } else {
            task.setAttempts(0L);
        }
        Node attemptsOk = map.getNamedItem(ATTEMPTS_OK);
        if (attemptsOk != null && attemptsOk.getTextContent() != null) {
            returnNull = false;
            task.setAttemptsOk(Long.parseLong(attemptsOk.getTextContent()));
        } else {
            task.setAttemptsOk(0L);
        }
        Node time = map.getNamedItem(TIME);
        if (time != null && time.getTextContent() != null) {
            returnNull = false;
            task.setTime(Long.parseLong(time.getTextContent()));
        } else {
            task.setTime(0L);
        }
        if (returnNull) {
            return null;
        }
        return task;
    }

    /**
     * delete task from xml
     * @param id Integer
     * @return boolean is deleted task
     */
    public boolean delete(Integer id) {
        Node node = getNodeById(id);
        if (node != null) {
            Node dashboard = getDashboard();
            dashboard.removeChild(node);
            try {
                flush();
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    private Node getDashboard() {
        NodeList nodeList = doc.getElementsByTagName(DASHBOARD);
        return nodeList.item(0);
    }

    public boolean save() {
        try {
            flush();
            return true;
        } catch (TransformerException e) {
            return false;
        }
    }

    /**
     * synchronized update task and save into xml
     * @param task Task
     * @return boolean is updated
     */
    public synchronized boolean updateSyncTask(Task task) {
        if (task == null) {
            return false;
        }
        Node node = getNodeById(task.getId());
        updateNode(node, task);
        try {
            flush();
            return true;
        } catch (TransformerException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * update node from task and save this
     * @param node Node
     * @param task Task
     */
    private void updateNode(Node node, Task task) {
        if (node == null || task == null) {
            return;
        }
        NamedNodeMap nodeMap = node.getAttributes();
        Node id = nodeMap.getNamedItem(ID);
        if (id == null) {
            Node newId = doc.createElement(ID);
            newId.setTextContent(task.getId().toString());
            nodeMap.setNamedItem(newId);
        } else {
            id.setTextContent(task.getId().toString());
        }
        Node name = nodeMap.getNamedItem(NAME);
        if (name == null) {
            Node newName = doc.createElement(NAME);
            newName.setTextContent(task.getName());
            nodeMap.setNamedItem(newName);
        } else {
            name.setTextContent(task.getName());
        }
        Node url = nodeMap.getNamedItem(URL);
        if (url == null) {
            Node newUrl = doc.createElement(URL);
            newUrl.setTextContent(task.getUrl());
            nodeMap.setNamedItem(newUrl);
        } else {
            url.setTextContent(task.getUrl());
        }
        Node attempts =nodeMap.getNamedItem(ATTEMPTS);
        if (attempts == null) {
            Node newAttempts = doc.createAttribute(ATTEMPTS);
            newAttempts.setTextContent(task.getAttempts().toString());
            nodeMap.setNamedItem(newAttempts);
        } else {
            attempts.setTextContent(task.getAttempts().toString());
        }
        Node attemptsOk = nodeMap.getNamedItem(ATTEMPTS_OK);
        if (attemptsOk == null) {
            Node newAttemptsOk = doc.createAttribute(ATTEMPTS_OK);
            newAttemptsOk.setTextContent(task.getAttemptsOk().toString());
            nodeMap.setNamedItem(newAttemptsOk);
        } else {
            attemptsOk.setTextContent(task.getAttemptsOk().toString());
        }
        Node time = nodeMap.getNamedItem(TIME);
        if (time == null) {
            Node newTime = doc.createAttribute(TIME);
            newTime.setTextContent(task.getTime().toString());
            nodeMap.setNamedItem(newTime);
        } else {
            time.setTextContent(task.getTime().toString());
        }

    }

    /**
     * get count thread from xml if errors return DEFAULT VALUE
     * @return int count thread
     */
    public int getCountThread() {
        NodeList threadsCount = doc.getElementsByTagName(THREADS);
        try {
            Node thread = threadsCount.item(threadsCount.getLength() - 1);
            return Integer.parseInt(thread.getAttributes().getNamedItem(COUNT).getTextContent());
        } catch (Exception e){
            return Config.COUNT_DEFAULT;
        }
    }
}
