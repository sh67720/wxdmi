-- MySQL dump 10.13  Distrib 5.6.24, for osx10.8 (x86_64)
--
-- Host: localhost    Database: wxdmi
-- ------------------------------------------------------
-- Server version	5.6.24

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(100) NOT NULL DEFAULT '' COMMENT '密码',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'admin','e10adc3949ba59abbe56e057f20f883e','2017-10-14 10:48:18','2017-10-14 10:48:18');
INSERT INTO `admin` VALUES (2,'zhuzhu','48bd42bfc96d6a0fb3fb678fb7feb13e','2017-10-14 10:48:18','2017-10-14 10:48:18');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `singlepicture`
--

DROP TABLE IF EXISTS `singlepicture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `singlepicture` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `type` tinyint(4) NOT NULL COMMENT '图片类型(1.首页轮播大图;2.首页轮播小图;3.一起创新大牌;4.案例业务介绍;5.首页简介;6.简介顶部背景;7.工作册)',
  `path` varchar(100) NOT NULL COMMENT '图片路径',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `singlepicture`
--

LOCK TABLES `singlepicture` WRITE;
/*!40000 ALTER TABLE `singlepicture` DISABLE KEYS */;
/*!40000 ALTER TABLE `singlepicture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `industryinformation`
--

DROP TABLE IF EXISTS `industryinformation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `industryinformation` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `coverpic` varchar(100) NOT NULL COMMENT '封面图片',
  `name` varchar(100) DEFAULT '' COMMENT '资讯名称',
  `date` varchar(100) DEFAULT '' COMMENT '资讯发布时间',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `industryinformation`
--

LOCK TABLES `industryinformation` WRITE;
/*!40000 ALTER TABLE `industryinformation` DISABLE KEYS */;
/*!40000 ALTER TABLE `industryinformation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `industryinformationpicture`
--

DROP TABLE IF EXISTS `industryinformationpicture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `industryinformationpicture` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `industryinformationId` int(11) unsigned NOT NULL COMMENT '资讯id',
  `path` varchar(100) NOT NULL COMMENT '图片地址',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `industryinformationpicture`
--

LOCK TABLES `industryinformationpicture` WRITE;
/*!40000 ALTER TABLE `industryinformationpicture` DISABLE KEYS */;
/*!40000 ALTER TABLE `industryinformationpicture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workcase`
--

DROP TABLE IF EXISTS `workcase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workcase` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '品牌名称',
  `category` tinyint(4) NOT NULL COMMENT '类别',
  `industry` tinyint(4) NOT NULL COMMENT '所属行业',
  `serviceContent` varchar(500) NOT NULL COMMENT '服务内容',
  `brandIntroduction` text COMMENT '品牌介绍',
  `homepageShow` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否在首页显示',
  `remark` text COMMENT '其他',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workcase`
--

LOCK TABLES `workcase` WRITE;
/*!40000 ALTER TABLE `workcase` DISABLE KEYS */;
/*!40000 ALTER TABLE `workcase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workcasepicture`
--

DROP TABLE IF EXISTS `workcasepicture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workcasepicture` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `workcaseId` int(11) unsigned NOT NULL COMMENT '品牌id',
  `path` varchar(100) NOT NULL COMMENT '图片地址',
  `iscover` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否是封面图片',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workcasepicture`
--

LOCK TABLES `workcasepicture` WRITE;
/*!40000 ALTER TABLE `workcasepicture` DISABLE KEYS */;
/*!40000 ALTER TABLE `workcasepicture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `companysummary`
--

DROP TABLE IF EXISTS `companysummary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `companysummary` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '公司总结名称',
  `summaryPic` varchar(100) NOT NULL COMMENT '公司总结图片',
  `summaryDetail` text COMMENT '公司总结详细',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `companysummary`
--

LOCK TABLES `companysummary` WRITE;
/*!40000 ALTER TABLE `companysummary` DISABLE KEYS */;
/*!40000 ALTER TABLE `companysummary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teammembers`
--

DROP TABLE IF EXISTS `teammembers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teammembers` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '成员姓名',
  `membersPosition` varchar(100) NOT NULL COMMENT '成员职务',
  `membersPic` varchar(100) NOT NULL COMMENT '成员照片',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teammembers`
--

LOCK TABLES `teammembers` WRITE;
/*!40000 ALTER TABLE `teammembers` DISABLE KEYS */;
/*!40000 ALTER TABLE `teammembers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recruit`
--

DROP TABLE IF EXISTS `recruit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recruit` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `bigTitle` varchar(100) NOT NULL COMMENT '招聘大标题',
  `smallTitle` varchar(100) NOT NULL COMMENT '招聘小标题',
  `address` varchar(500) NOT NULL COMMENT '招聘地址',
  `wordContent` varchar(1000) NOT NULL COMMENT '招聘工作内容',
  `jobRequirements` varchar(1000) NOT NULL COMMENT '招聘岗位要求',
  `recruitCity` varchar(100) NOT NULL COMMENT '招聘工作城市',
  `applyMethod` varchar(100) NOT NULL COMMENT '招聘应聘方式',
  `recruitPic` varchar(100) NOT NULL COMMENT '招聘图标',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recruit`
--

LOCK TABLES `recruit` WRITE;
/*!40000 ALTER TABLE `recruit` DISABLE KEYS */;
/*!40000 ALTER TABLE `recruit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `companyabout`
--

DROP TABLE IF EXISTS `companyabout`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `companyabout` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `companyCity` varchar(100) NOT NULL COMMENT '公司所在城市',
  `companyCityEngName` varchar(100) NOT NULL COMMENT '公司所在城市英文名',
  `companyAddress` varchar(500) NOT NULL COMMENT '公司地址',
  `companyCityPic` varchar(100) NOT NULL COMMENT '公司所在城市图标',
  `companyIsHQ` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否总部',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `companyabout`
--

LOCK TABLES `companyabout` WRITE;
/*!40000 ALTER TABLE `companyabout` DISABLE KEYS */;
/*!40000 ALTER TABLE `companyabout` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `clientCompanyName` varchar(100) NOT NULL COMMENT '客户公司名称',
  `clientName` varchar(100) NOT NULL COMMENT '客户姓名',
  `clientTel` varchar(100) NOT NULL COMMENT '客户联系电话',
  `clientCity` varchar(100) NOT NULL COMMENT '客户所在城市',
  `requirement` text COMMENT '客户需求',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-20 10:50:19